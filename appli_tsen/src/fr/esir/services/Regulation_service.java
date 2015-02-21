package fr.esir.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.example.esir.nsoc2014.regulator.regulation.Regulator;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.oep.RepetetiveTask;
import fr.esir.regulation.NbPerson;
import fr.esir.resources.FilterString;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Regulation_service extends Service {
    private final static String TAG = Regulation_service.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    public RepetetiveTask rt;
    private List<DatesInterval> listConsigne;
    private final BroadcastReceiver mServicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            Bundle bundle;
            switch (action) {
                case FilterString.OEP_DATA_CONSIGNES_OF_DAY:
                    Log.w(TAG, "reception consignes ok");
                    bundle = intent.getBundleExtra("Data");
                    if (bundle != null) {
                        listConsigne = (List<DatesInterval>) bundle.getSerializable("List");
                        sortList(listConsigne);
                    }
            }
        }
    };
    private Regulator regulator;
    private int nb_users;
    private ArrayList<NbPerson> nbperson;

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        //students of the day, coming from oep
        intentFilter.addAction((FilterString.OEP_DATA_CONSIGNES_OF_DAY));

        return intentFilter;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        rt.getScheduler().shutdown();
        rt = null;
        unregisterReceiver(mServicesUpdateReceiver);
        regulator.stop();
        regulator = null;
        return super.onUnbind(intent);
    }

    public void setAlarm30B4(DatesInterval entry) {
        long startDate = entry.getStartDate().getTime();
        //30 minutes before the lesson
        long min30B4StartDate = startDate - (30 * 60 * 1000);
        Log.w("StartDate", min30B4StartDate + "");
        double cons = entry.getConsigne();
        double nb_pers = entry.getNbPerson();
        long currentDate = System.currentTimeMillis();

        String s = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((startDate - currentDate) - min30B4StartDate),
                TimeUnit.MILLISECONDS.toSeconds((startDate - currentDate) - min30B4StartDate) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((startDate - currentDate) - min30B4StartDate))
        );

        Log.i("TIME MIN", s);
        //start a task 30 minutes before the lesson = predict the heat time
        rt = new RepetetiveTask((startDate - currentDate) - min30B4StartDate, cons, nb_pers, entry.getEndDate());
    }

    private void sortList(List<DatesInterval> l) {
        Collections.sort(l, new Comparator<DatesInterval>() {
            @Override
            public int compare(DatesInterval lhs, DatesInterval rhs) {
                return lhs.getStartDate().getTime() < rhs.getStartDate().getTime() ?
                        -1 : lhs.getStartDate().getTime() > rhs.getStartDate().getTime() ?
                        1 : 0;
            }
        });
        nbperson = new ArrayList<>();
        long previousEndDate = 0;
        for (DatesInterval entry : l) {
            Log.w(TAG, "Between " + entry.getStartDate() + " and " + entry.getEndDate()
                    + " the temperature in the classroom " + entry.getLesson()
                    + " must be " + entry.getConsigne() + " °C");
            setAlarm30B4(entry);
            nbperson.add(new NbPerson(entry.getStartDate().getTime(), entry.getEndDate().getTime(), entry.getNbPerson()));
            if (l.indexOf(entry) == 0)
                nb_users = entry.getNbPerson();
            if (l.indexOf(entry) == l.size() - 1)
                setAlarmEndLesson(entry.getEndDate(), 15);
            else {
                if (entry.getStartDate().getTime() - previousEndDate > 35 * 60000)
                    setAlarmEndLesson(entry.getEndDate(), 20);
            }
            previousEndDate = entry.getEndDate().getTime();
        }
    }

    private void setAlarmEndLesson(Date finalEnd, double consigne) {
        new RepetetiveTask(finalEnd.getTime() - System.currentTimeMillis() - 5 * 60000, consigne, RepetetiveTask.FINALCONS);
    }

    public boolean initialize() {
        registerReceiver(mServicesUpdateReceiver, makeServicesUpdateIntentFilter());
        regulator = new Regulator();
        regulator.setConsigne(18);
        //regulator.run();
        return true;
    }

    private int checkNbPerson(long currentDate) {
        for (NbPerson entry : nbperson) {
            if (currentDate >= entry.getStartDate() && currentDate <= entry.getEndDate())
                return entry.getNb_pers();
        }
        return 1;
    }

    public void executeVote(double value, String vote) {
        double val = value;
        int nb = checkNbPerson(System.currentTimeMillis());
        switch (vote) {
            case "++":
                val = val - (1 / nb);
                break;
            case "+":
                val = val - (0.5 / nb);
                break;
            case "-":
                val = val + (0.5 / nb);
                break;
            case "--":
                val = val + (1 / nb);
                break;
        }
        regulator.setConsigne(val);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public Regulation_service getService() {
            return Regulation_service.this;
        }
    }
}
