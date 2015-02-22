package fr.esir.maintasks;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import com.example.esir.nsoc2014.tsen.lob.interfaces.Service_oep;
import fr.esir.fragments.MainFragment;
import fr.esir.knx.Service_knx;
import fr.esir.oep.RepetetiveTask;
import fr.esir.resources.FilterString;
import fr.esir.services.Context_service;
import fr.esir.services.Knx_service;
import fr.esir.services.Oep_service;
import fr.esir.services.Regulation_service;

public class MyActivity extends Activity {
    private final static String TAG = MyActivity.class.getSimpleName();
    public static Service_oep mOep_service;
    public static double lastHum_in = 0;
    public static double lastHum_out = 0;
    public static double lastTemp_in = 0;
    ;
    public static double lastTemp_out = 0;
    public static double lastLum_out = 0;
    public static double lastCO2 = 0;
    public static Context ct;
    public static Context_service mContext_service;
    public static Regulation_service mRegulation_service;
    public static MainFragment mp;
    private final BroadcastReceiver mServicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            switch (action) {
                case FilterString.RECEIVE_DATA_KNX:
                    setDisplayData(intent.getStringExtra("ADDRESS"), intent.getStringExtra("DATA"));
            }
        }
    };
    public Service_knx mKnx_service;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //there 4 services so we need to know which is started
            switch (name.getClassName()) {
                case "fr.esir.services.Context_service":
                    mContext_service = ((Context_service.LocalBinder) service).getService();
                    if (!mContext_service.initialize()) {
                        Log.e(TAG, "Unable to initialize the context");
                        finish();
                    }
                    break;
                case "fr.esir.services.Oep_service":
                    mOep_service = ((Oep_service.LocalBinder) service).getService();
                    if (!mOep_service.initialize()) {
                        Log.e(TAG, "Unable to initialize the oep");
                        finish();
                    }
                    Log.w(TAG, "Oep initialized");
                    break;
                case "fr.esir.services.Regulation_service":
                    mRegulation_service = ((Regulation_service.LocalBinder) service).getService();
                    if (!mRegulation_service.initialize()) {
                        Log.e(TAG, "Unable to initialize the regulation");
                        finish();
                    }
                    break;
                case "fr.esir.services.Knx_service":
                    mKnx_service = ((Knx_service.LocalBinder) service).getService();
                    if (!mKnx_service.initialize()) {
                        Log.e(TAG, "Unable to initialize KNX");
                        finish();
                    }
                    break;
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            switch (name.getClassName()) {
                case "fr.esir.services.Context_service":
                    mContext_service = null;
                    break;
                case "fr.esir.services.Oep_service":
                    mOep_service = null;
                    break;
                case "fr.esir.services.Regulation_service":
                    mRegulation_service = null;
                    break;
                case "fr.esir.services.Knx_service":
                    mKnx_service = null;
                    break;
            }
        }
    };
    SharedPreferences pref;
    TextView next_prog;
    private boolean checking = RepetetiveTask.checking;
    private double consigneTemp = RepetetiveTask.consigne;

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FilterString.RECEIVE_DATA_KNX);

        return intentFilter;
    }

    private void broadcastUpdate(final String action, double cons, long timeEnd) {
        final Intent intent = new Intent(action);
        intent.putExtra("CONSIGNE", cons);
        intent.putExtra("TIME", timeEnd);
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
        ct = this;
        pref = getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
        FragmentManager fm = getFragmentManager();
        mp = new MainFragment();
        fm.beginTransaction().add(R.id.containerMain, mp).commit();

        next_prog = (TextView) findViewById(R.id.tvProg);

    }

    public void setTvProg(String string) {
        pref.edit().putString("TVPROG", string).apply();

    }

    private void bindServices() {

        // start the service context_service
        Intent contextServiceIntent = new Intent(this.getApplicationContext(), Context_service.class);
        bindService(contextServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // start the service oep_service
        //condition : the contexte_service is working
        Intent oepServiceIntent = new Intent(this.getApplicationContext(), Oep_service.class);
        bindService(oepServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // start the service regulation_service
        Intent regulationServiceIntent = new Intent(this.getApplicationContext(), Regulation_service.class);
        bindService(regulationServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        // start the service knx_service
        Intent knxServiceIntent = new Intent(this.getApplicationContext(), Knx_service.class);
        bindService(knxServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(mServiceConnection);
        mContext_service = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mServiceConnection);
        unregisterReceiver(mServicesUpdateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindServices();
        registerReceiver(mServicesUpdateReceiver, makeServicesUpdateIntentFilter());
    }

    private void setDisplayData(String add, String data) {
        Log.i(TAG, "adresse " + add + " data " + data);
        //variables lastX contain the last data of sensors
        String[] dataSplit = data.split(" ");
        if (add.equals("0/0/4")) { //co2 sensor
            lastCO2 = Double.parseDouble(dataSplit[0]);
        } else if (add.equals("0/1/0")) { //outdoor temperature sensor
            lastTemp_out = Double.parseDouble(dataSplit[0]);
        } else if (add.equals("0/0/5")) {//indoor humidity sensor
            lastHum_in = Double.parseDouble(dataSplit[0]);
        } else if (add.equals("0/1/1")) {//outdoor luminosity sensor
            lastLum_out = Double.parseDouble(dataSplit[0]);
        } else if (add.equals("0/1/2")) { //outdoor humidity sensor
            lastHum_out = Double.parseDouble(dataSplit[0]);
        } else if (add.equals("0/0/3")) { //indoor temperature sensor
            lastTemp_in = Double.parseDouble(dataSplit[0]);
            if (checking) {
                if (lastTemp_in >= consigneTemp)
                    broadcastUpdate(FilterString.REGULATION_EXTRA_DATA, consigneTemp, System.currentTimeMillis());

            }
        }
        mp.setDisplayData(add, data);
    }
}
