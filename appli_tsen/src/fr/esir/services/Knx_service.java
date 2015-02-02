package fr.esir.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class Knx_service extends Service {
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public Knx_service getService() {
            return Knx_service.this;
        }
    }

    public boolean initialize() {

        return false;
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
}
