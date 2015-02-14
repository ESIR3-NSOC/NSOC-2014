package fr.esir.knx;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by Nicolas on 14/02/2015.
 */
public class AsyncKNX extends AsyncTask<KnxManager, Void, Boolean> {
    KnxManager cc = null;

    @Override
    protected Boolean doInBackground(KnxManager... params) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        cc = params[0];
        if (cc.initConnection())
            return true;
        else
            return false;
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        if (bool) {
            cc.createKNXListener();
            Log.w("KNX State", "connected");
        } else Log.w("KNX State", "disconnected");
    }
}
