package fr.esir.services;

import android.app.Service;
import android.content.*;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.regulation.interface_service.Service_regulation;
import fr.esir.ressources.FilterString;

import java.util.List;

public class Regulation_service extends Service implements Service_regulation {
    private final static String TAG = Context_service.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();

    private SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyPrefs" ;

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
                    Log.w(TAG, "reception sonsignes ok");
                    bundle = intent.getBundleExtra("Data");
                    if (bundle != null) {
                        List<DatesInterval> listConsigne = (List<DatesInterval>) bundle.getSerializable("List");
                        for (DatesInterval entry : listConsigne) {
                            Log.w(TAG, "Between " + entry.getStartDate() + " and " + entry.getStartEnd()
                                    + " the temperature in the classroom " + entry.getLesson()
                                    + " must be " + entry.getConsigne() + " °C");
                        }
                    }
            }
        }
    };

    public boolean initialize() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        registerReceiver(mServicesUpdateReceiver, makeServicesUpdateIntentFilter());

        return true;
    }

    public SharedPreferences getSharedpreferences(){
        return sharedpreferences;
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
