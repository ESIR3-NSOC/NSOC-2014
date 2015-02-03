package fr.esir.maintasks;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import fr.esir.oep.PredictBroadcastReceiver;
import fr.esir.ressources.FilterString;
import fr.esir.services.Context_service;
import fr.esir.services.Knx_service;
import fr.esir.services.Oep_service;
import fr.esir.services.Regulation_service;

import java.util.Calendar;

public class MyActivity extends Activity {
    private final static String TAG = MyActivity.class.getSimpleName();
    public Context_service mContext_service;
    public Oep_service mOep_service;
    public Knx_service mKnx_service;
    public Regulation_service mRegulation_service;
    TextView tv;


    /*
    * serviceConnection : context_service
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            tv.setText(name.getClassName());
            //there 4 services so we need to know which is started
            switch(name.getClassName()){
                case "fr.esir.services.Context_service" :
                    mContext_service = ((Context_service.LocalBinder) service).getService();
                    break;
                case "fr.esir.services.Oep_service" :
                    mOep_service = ((Oep_service.LocalBinder) service).getService();
                    if (!mOep_service.initialize()) {
                        Log.e(TAG, "Unable to initialize the oep");
                        finish();
                    }
                    Log.w(TAG, "Oep initialized");
                    break;
                case "fr.esir.services.Regulation_service" :
                    mRegulation_service = ((Regulation_service.LocalBinder) service).getService();
                    break;
                case "fr.esir.services.Knx_service" :
                    mKnx_service = ((Knx_service.LocalBinder) service).getService();
                    break;
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mContext_service = null;
        }
    };

    private final BroadcastReceiver mServicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv = (TextView) findViewById(R.id.tv);

        // start the service context_service
        //Intent contextServiceIntent = new Intent(this.getApplicationContext(), Context_service.class);
        //bindService(contextServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // start the service oep_service
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
        //unregisterReceiver(mServicesUpdateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(mServicesUpdateReceiver, makeServicesUpdateIntentFilter());
    }

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FilterString.ACTION_CONTEXT_CONNECTED);
        intentFilter.addAction(FilterString.ACTION_CONTEXT_DISCONNECTED);
        intentFilter.addAction(FilterString.CONTEXT_EXTRA_DATA);

        intentFilter.addAction(FilterString.ACTION_REGULATION_CONNECTED);
        intentFilter.addAction(FilterString.ACTION_REGULATION_DISCONNECTED);
        intentFilter.addAction(FilterString.REGULATION_EXTRA_DATA);

        intentFilter.addAction(FilterString.ACTION_KNX_CONNECTED);
        intentFilter.addAction(FilterString.ACTION_KNX_DISCONNECTED);
        intentFilter.addAction(FilterString.KNX_EXTRA_DATA);

        intentFilter.addAction(FilterString.ACTION_OEP_CONNECTED);
        intentFilter.addAction(FilterString.ACTION_OEP_DISCONNECTED);
        intentFilter.addAction(FilterString.OEP_EXTRA_DATA);

        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        return intentFilter;
    }
}
