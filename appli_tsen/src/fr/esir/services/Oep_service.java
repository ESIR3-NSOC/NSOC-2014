package fr.esir.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import fr.esir.oep.PredictBroadcastReceiver;

import java.util.Calendar;

public class Oep_service extends Service {
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
        public Oep_service getService() {
            return Oep_service.this;
        }
    }

    public boolean initialize() {
        //oep alarm manager
        //create new calendar instance
        Calendar sixAMCalendar = Calendar.getInstance();
        sixAMCalendar.setTimeInMillis(System.currentTimeMillis());
        //set the time to 1AM
        sixAMCalendar.set(Calendar.HOUR_OF_DAY, 0);
        sixAMCalendar.set(Calendar.MINUTE, 6);
        sixAMCalendar.set(Calendar.SECOND, 0);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //create a pending intent to be called at midnight
        Intent sixI = new Intent(this, PredictBroadcastReceiver.class);
        PendingIntent sixAMPI = PendingIntent.getBroadcast(this, 0, sixI, PendingIntent.FLAG_UPDATE_CURRENT);
        //schedule time for pending intent, and set the interval to day so that this event will repeat at the selected time every day
        am.setRepeating(AlarmManager.RTC_WAKEUP, sixAMCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sixAMPI);
        return true;
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
}
