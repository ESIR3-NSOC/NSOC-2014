package fr.esir.maintasks;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import fr.esir.services.Context_service;

public class MyActivity extends Activity {
    private final static String TAG = MyActivity.class.getSimpleName();
    private Context_service mContext_service;

    /*
    * serviceConnection : context_service
     */
    private final ServiceConnection mServiceConnectionContext = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (!mContext_service.initialize()) {
                Log.e(TAG, "Unable to initialize the context");
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mContext_service = null;
        }
    };

    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {

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

        // start the service context_service
        Intent contextServiceIntent = new Intent(this, Context_service.class);
        bindService(contextServiceIntent, mServiceConnectionContext, BIND_AUTO_CREATE);
    }
}
