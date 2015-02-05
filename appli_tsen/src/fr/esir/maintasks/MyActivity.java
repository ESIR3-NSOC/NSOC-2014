package fr.esir.maintasks;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import com.example.esir.nsoc2014.tsen.lob.interfaces.Service_oep;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.ressources.FilterString;
import fr.esir.services.Context_service;
import fr.esir.services.Knx_service;
import fr.esir.services.Oep_service;
import fr.esir.services.Regulation_service;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivity extends Activity {
    private final static String TAG = MyActivity.class.getSimpleName();
    public Context_service mContext_service;
    public static Service_oep mOep_service;
    public Knx_service mKnx_service;
    public Regulation_service mRegulation_service;
    TextView context_state;
    TextView oep_state;
    TextView regulation_state;
    TextView knx_state;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //there 4 services so we need to know which is started
            switch (name.getClassName()) {
                case "fr.esir.services.Context_service":
                    mContext_service = ((Context_service.LocalBinder) service).getService();
                    context_state.setText(R.string.connected);
                    Log.e(TAG,"not here");
                    break;
                case "fr.esir.services.Oep_service":
                    mOep_service = ((Oep_service.LocalBinder) service).getService();
                    if (!mOep_service.initialize()) {
                        Log.e(TAG, "Unable to initialize the oep");
                        finish();
                    }
                    oep_state.setText(R.string.connected);
                    Log.w(TAG, "Oep initialized");
                    break;
                case "fr.esir.services.Regulation_service":
                    mRegulation_service = ((Regulation_service.LocalBinder) service).getService();
                    regulation_state.setText(R.string.connected);
                    break;
                case "fr.esir.services.Knx_service":
                    mKnx_service = ((Knx_service.LocalBinder) service).getService();
                    knx_state.setText(R.string.connected);
                    break;
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            switch (name.getClassName()) {
                case "fr.esir.services.Context_service":
                    mContext_service = null;
                    context_state.setText(R.string.disconnected);
                    break;
                case "fr.esir.services.Oep_service":
                    mOep_service = null;
                    oep_state.setText(R.string.disconnected);
                    break;
                case "fr.esir.services.Regulation_service":
                    mRegulation_service = null;
                    regulation_state.setText(R.string.disconnected);
                    break;
                case "fr.esir.services.Knx_service":
                    mKnx_service = null;
                    knx_state.setText(R.string.disconnected);
                    break;
            }
        }
    };

    private final BroadcastReceiver mServicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            //test broadcast oep
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
            //end test broadcast oep
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String action, String key, Bundle data) {
        final Intent intent = new Intent(action);
        intent.putExtra(key, data);
        sendBroadcast(intent);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        context_state = (TextView) findViewById(R.id.context_state);
        oep_state = (TextView) findViewById(R.id.oep_state);
        regulation_state = (TextView) findViewById(R.id.regulation_state);
        knx_state = (TextView) findViewById(R.id.knx_state);

        // start the service context_service
        //Intent contextServiceIntent = new Intent(this.getApplicationContext(), Context_service.class);
        //bindService(contextServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // start the service oep_service
        //condition : the contexte_service is working
        Intent oepServiceIntent = new Intent(this.getApplicationContext(), Oep_service.class);
        bindService(oepServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // start the service regulation_service
        //Intent regulationServiceIntent = new Intent(this.getApplicationContext(), Regulation_service.class);
        //bindService(regulationServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // start the service knx_service
        //Intent knxServiceIntent = new Intent(this.getApplicationContext(), Knx_service.class);
        //bindService(knxServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mContext_service = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mServicesUpdateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mServicesUpdateReceiver, makeServicesUpdateIntentFilter());
    }

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FilterString.CONTEXT_EXTRA_DATA);
        intentFilter.addAction(FilterString.REGULATION_EXTRA_DATA);
        intentFilter.addAction(FilterString.OEP_EXTRA_DATA);

        //filters to receive from oep service
        intentFilter.addAction((FilterString.OEP_DATA_STUDENTS_OF_DAY));
        intentFilter.addAction((FilterString.OEP_DATA_CONSIGNES_OF_DAY));

        return intentFilter;
    }
}
