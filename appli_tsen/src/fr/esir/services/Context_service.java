package fr.esir.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import context.Context;
import tsen.TsenUniverse;

public class Context_service extends Service {

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        registerReceiver(mServicesUpdateReceiver, makeServicesUpdateIntentFilter());
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public Context_service getService() {
            return Context_service.this;
        }
    }

    public boolean initialize() {
        Context ctx = new Context(new TsenUniverse());
        ctx.startContext();
        return true;
    }

    private void broadcastUpdate(String action, Object object) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private final BroadcastReceiver mServicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, Intent intent) {
            final String action = intent.getAction();

        }
    };

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        return intentFilter;
    }
}
