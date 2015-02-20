package fr.esir.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.oep.RepetetiveTask;
import fr.esir.resources.FilterString;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Regulation_service extends Service {
    private final static String TAG = Context_service.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private List<DatesInterval> listConsigne;
    public RepetetiveTask rt;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        rt = null;
        unregisterReceiver(mServicesUpdateReceiver);
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public Regulation_service getService() {
            return Regulation_service.this;
        }
    }

    private final BroadcastReceiver mServicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            Bundle bundle;
            switch (action) {
                case FilterString.OEP_DATA_CONSIGNES_OF_DAY:
                    Log.w(TAG, "reception consignes ok");
                    bundle = intent.getBundleExtra("Data");
                    if (bundle != null) {
                        listConsigne = (List<DatesInterval>) bundle.getSerializable("List");
                        sortList(listConsigne);
                    }
            }
        }
    };

    public void setAlarm30B4(DatesInterval entry) {
        long startDate = entry.getStartDate().getTime();
        //30 minutes before the lesson
        long min30B4StartDate = startDate - (30 * 60 * 1000);

        double cons = entry.getConsigne();
        long currentDate = System.currentTimeMillis();

        //start a task 30 minutes before the lesson = predict the heat time
        rt = new RepetetiveTask((startDate - currentDate) - min30B4StartDate, cons, entry.getEndDate());
    }

    private void sortList(List<DatesInterval> l) {
        Collections.sort(l, new Comparator<DatesInterval>() {
            @Override
            public int compare(DatesInterval lhs, DatesInterval rhs) {
                return lhs.getStartDate().getTime() < rhs.getStartDate().getTime() ?
                        -1 : lhs.getStartDate().getTime() > rhs.getStartDate().getTime() ?
                        1 : 0;
            }
        });

        for (DatesInterval entry : l) {
            Log.w(TAG, "Between " + entry.getStartDate() + " and " + entry.getEndDate()
                    + " the temperature in the classroom " + entry.getLesson()
                    + " must be " + entry.getConsigne() + " °C");
            setAlarm30B4(entry);
        }
    }

    public boolean initialize() {
        registerReceiver(mServicesUpdateReceiver, makeServicesUpdateIntentFilter());

        return true;
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        //students of the day, coming from oep
        intentFilter.addAction((FilterString.OEP_DATA_CONSIGNES_OF_DAY));

        return intentFilter;
    }
}
