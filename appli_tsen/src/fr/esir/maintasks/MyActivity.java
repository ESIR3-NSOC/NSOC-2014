package fr.esir.maintasks;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import fr.esir.ressources.FilterString;
import fr.esir.services.Context_service;

public class MyActivity extends Activity {
    private final static String TAG = MyActivity.class.getSimpleName();
    public Context_service mContext_service;
    TextView tv;

    /*
    * serviceConnection : context_service
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mContext_service = ((Context_service.LocalBinder) service).getService();
            Log.w(TAG, name.getClassName().toString());
            tv.setText(name.getClassName().toString());
            //there 4 services so we need to know which is started.
            if (name.getClassName().toString().equals("fr.esir.services.Context_service")) {
                if (!mContext_service.initialize()) {
                    Log.e(TAG, "Unable to initialize the context");
                    finish();
                }
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

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv = (TextView) findViewById(R.id.tv);
        // start the service context_service
        Intent contextServiceIntent = new Intent(this.getApplicationContext(), Context_service.class);
        bindService(contextServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
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

        intentFilter.addAction(android.content.Intent.ACTION_TIME_TICK);
        return intentFilter;
    }
}
