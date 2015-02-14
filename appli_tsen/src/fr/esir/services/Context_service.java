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
import org.codehaus.jackson.JsonNode;


import context.Context;
import tsen.TsenUniverse;

import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;

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

    private void broadcastUpdate(String action, Object object) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private final BroadcastReceiver mServicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, Intent intent) {
            final String action = intent.getAction();
            Bundle bundle;
            switch (action) {
                case FilterString.OEP_DATA_STUDENTS_OF_DAY:
                    Log.w(TAG, "reception students ok");
                    bundle = intent.getBundleExtra("Data");
                    if (bundle != null) {
                        HashMap<Time, List<DatesInterval>> map = (HashMap<Time, List<DatesInterval>>) bundle.getSerializable("HashMap");
                        for (Map.Entry<Time, List<DatesInterval>> entry : map.entrySet()) {
                            Log.w(TAG, entry.getKey() + "");
                            for (DatesInterval entryDi : entry.getValue()) {
                                Log.w(TAG, entryDi.getId() + " will be in classroom " + entryDi.getLesson()
                                        + ". His consigne must be " + entryDi.getConsigne() + " °C");
                            }
                        }
                    }
                    break;
            }

            switch(action){
                case FilterString.CONTEXT_INIT_SENSOR : ctx.initSensors((JsonNode)intent.getExtras().get("plop")); break;
                case FilterString.CONTEXT_UPDATE_VALUE : break;
            }



        }
    };

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        //students of the day, coming from oep
        intentFilter.addAction((FilterString.OEP_DATA_STUDENTS_OF_DAY));

        return intentFilter;
    }
}
