package fr.esir.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import fr.esir.ressources.FilterString;


import context.Context;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import tsen.Room;
import tsen.TsenUniverse;

import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import tsen.TsenView;
import tsen.User;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context_service extends Service {

    private final static String TAG = Context_service.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private Context ctx;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        unregisterReceiver(mServicesUpdateReceiver);
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public Context_service getService() {
            return Context_service.this;
        }
    }

    public boolean initialize() {

        if(ctx!=null){
            ctx = new Context(new TsenUniverse());
            ctx.startContext();
            return true;
        }else{

        }
        ctx = new Context(new TsenUniverse());
        ctx.startContext();
        registerReceiver(mServicesUpdateReceiver, makeServicesUpdateIntentFilter());

        /*Context ctx = new Context(new TsenUniverse());
        ctx.startContext();*/
        return true;
    }

    private void broadcastUpdate(Intent intent) {
        sendBroadcast(intent);
    }

    private final BroadcastReceiver mServicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, Intent intent) {
            final String action = intent.getAction();
            Bundle bundle;
            switch (action) {
                case FilterString.OEP_DATA_STUDENTS_OF_DAY:
                    Log.i(TAG, "reception students ok");
                    bundle = intent.getBundleExtra("Data");
                    if (bundle != null) {
                        HashMap<Time, List<DatesInterval>> map = (HashMap<Time, List<DatesInterval>>) bundle.getSerializable("HashMap");
                        for (Map.Entry<Time, List<DatesInterval>> entry : map.entrySet()) {
                            Log.w(TAG, entry.getKey() + "");
                            for (DatesInterval entryDi : entry.getValue()) {
                                Log.w(TAG, entryDi.getId() + " will be in classroom " + entryDi.getLesson()
                                        + ". His consigne must be " + entryDi.getConsigne() + " °C");
                                addStudent(entryDi.getStartDate().getTime(),entryDi.getStartEnd().getTime(),entryDi.getId(),entryDi.getConsigne(),entryDi.getLesson());
                            }
                        }
                    }
                    break;

            }
        }
    };

    private void addStudent(long start, long end, String studentId, double consigne, String lesson){
        TsenView startView = ctx.getDimension().time(start);

        User user = startView.createUser();
        user.setId(studentId);
        user.setLesson(lesson);
        user.setTargetTemp(consigne);


        startView.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if(kObjects!=null && kObjects.length!=0){
                    Room room = (Room)kObjects[0];
                    room.addMember(user);
                }
            }
        });
        TsenView endView = ctx.getDimension().time(end);

        startView.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if(kObjects!=null && kObjects.length!=0){
                    Room room = (Room)kObjects[0];
                    room.removeMember(user);
                }
            }
        });
    }

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        //students of the day, coming from oep
        intentFilter.addAction((FilterString.OEP_DATA_STUDENTS_OF_DAY));

        return intentFilter;
    }


}
