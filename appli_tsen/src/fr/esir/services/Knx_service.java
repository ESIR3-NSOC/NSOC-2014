package fr.esir.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import fr.esir.knx.AsyncKNX;
import fr.esir.knx.KnxManager;
import fr.esir.resources.FilterString;
import knx.Service_knx;

public class Knx_service extends Service implements Service_knx {
    private KnxManager km = new KnxManager();
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        km.CloseConnection();
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public Knx_service getService() {
            return Knx_service.this;
        }
    }

    public boolean initialize() {
        AsyncKNX aknx = new AsyncKNX();
        aknx.execute(km);
        return true;
    }

    public void sendDisplayData(String add, String data) {
        final Intent intent = new Intent(FilterString.RECEIVE_DATA_KNX);
        intent.putExtra("DATA",data);
        intent.putExtra("ADDRESS",add);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
}
