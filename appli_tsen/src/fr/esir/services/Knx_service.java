package fr.esir.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import fr.esir.knx.AsyncKNX;
import fr.esir.knx.KnxManager;
import fr.esir.knx.Service_knx;
import fr.esir.resources.FilterString;
import tuwien.auto.calimero.exception.KNXException;

import java.io.IOException;

public class Knx_service extends Service implements Service_knx {
    private final IBinder mBinder = new LocalBinder();
    private KnxManager km;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        km.CloseConnection();
        return super.onUnbind(intent);
    }

    public boolean initialize() {
        try {
            km = new KnxManager(getAssets().open("knxGroup.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        AsyncKNX aknx = new AsyncKNX();
        aknx.execute(km);
        return true;
    }

    public void sendDisplayData(String add, String data) {
        final Intent intent = new Intent(FilterString.RECEIVE_DATA_KNX);
        intent.putExtra("DATA", data);
        intent.putExtra("ADDRESS", add);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    public void setVanne(int percent, String address) throws KNXException {
        km.setVanne(percent, address);
    }

    public class LocalBinder extends Binder {
        public Knx_service getService() {
            return Knx_service.this;
        }
    }
}
