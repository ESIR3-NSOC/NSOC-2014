package fr.esir.services;

import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.control.DataFromKNX;
import fr.esir.control.DataLearning;
import fr.esir.control.NbPerson;
import fr.esir.control.Regulator;
import fr.esir.fragments.DayProgram;
import fr.esir.knx.Reference;
import fr.esir.maintasks.MyActivity;
import fr.esir.maintasks.R;
import fr.esir.oep.RepetetiveTask;
import fr.esir.resources.FilterString;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Control_service extends Service {
    private final static String TAG = Control_service.class.getSimpleName();
    public static ArrayList<DatesInterval> list;
    public static Regulator regulator;
    private final IBinder mBinder = new LocalBinder();
    public RepetetiveTask rt;
    private ArrayList<DatesInterval> listConsigne;
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
                        listConsigne = (ArrayList<DatesInterval>) bundle.getSerializable("List");
                        sortList(listConsigne);
                    }
                    break;
                case FilterString.WEBSOCKET_VOTE_UPDATE:
                    String extra = intent.getStringExtra("VOTE");
                    try {
                        String vote = new ObjectMapper().readTree(extra).get("vote").asText();
                        executeVote(MyActivity.lastTemp_in, vote);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case FilterString.REGULATION_EXTRA_DATA:
                    double cons = intent.getDoubleExtra("CONSIGNE", 21);
                    long timeEnd = intent.getLongExtra("TIME", 600000);
                    putInArff(cons, timeEnd);
            }
        }
    };
    private int nb_users;
    private ArrayList<NbPerson> nbperson;

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        //students of the day, coming from oep
        intentFilter.addAction(FilterString.OEP_DATA_CONSIGNES_OF_DAY);
        intentFilter.addAction(FilterString.WEBSOCKET_VOTE_UPDATE);

        return intentFilter;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (rt != null)
            rt.getScheduler().shutdown();
        rt = null;
        unregisterReceiver(mServicesUpdateReceiver);
        regulator.cancel(false);
        regulator = null;
        return super.onUnbind(intent);
    }

    public void setAlarm30B4(DatesInterval entry) {
        long startDate = entry.getStartDate().getTime();
        //30 minutes before the lesson
        long min30B4StartDate = (30 * 60 * 1000);
        Log.w("StartDate", min30B4StartDate + "");
        Log.w("Startdate", entry.getStartDate() + "");
        double cons = entry.getConsigne();
        double nb_pers = entry.getNbPerson();
        long currentDate = System.currentTimeMillis();
        Log.w("currentDate", new Date(currentDate) + "");

        Log.i("SOUSTRACTION", (startDate - currentDate) - min30B4StartDate + "");
        String s = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(startDate - currentDate - min30B4StartDate),
                TimeUnit.MILLISECONDS.toSeconds(startDate - currentDate - min30B4StartDate) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startDate - currentDate - min30B4StartDate))
        );

        Log.i("TIME MIN", s);
        //start a task 30 minutes before the lesson = predict the heat time
        if ((startDate - currentDate) - min30B4StartDate > 0)
            rt = new RepetetiveTask((startDate - currentDate) - min30B4StartDate, cons, nb_pers, entry.getEndDate());
    }

    private void sortList(ArrayList<DatesInterval> l) {
        Collections.sort(l, new Comparator<DatesInterval>() {
            @Override
            public int compare(DatesInterval lhs, DatesInterval rhs) {
                return lhs.getStartDate().getTime() < rhs.getStartDate().getTime() ?
                        -1 : lhs.getStartDate().getTime() > rhs.getStartDate().getTime() ?
                        1 : 0;
            }
        });
        list = l;
        Fragment currentFragment = MyActivity.act.getFragmentManager().findFragmentById(R.id.containerMain);
        if (currentFragment instanceof DayProgram) {
            ((DayProgram) currentFragment).updateList(l);
        }
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
                setAlarmEndLesson(entry.getEndDate(), Reference.END_OF_DAY_TEMPERATURE);
            else {
                if (entry.getStartDate().getTime() - previousEndDate > Reference.TIME_MIN_TO_CONSIDERE_A_LONG_PAUSE)
                    setAlarmEndLesson(entry.getEndDate(), Reference.PAUSE_BTW_LESSONS_TEMPERATURE);
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
        regulator.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        regulator.setConsigne(20);
        //regulator.execute();
        //regulator.run();

        return true;
    }

    private int checkNbPerson(long currentDate) {
        if (nbperson != null) {
            for (NbPerson entry : nbperson) {
                if (currentDate >= entry.getStartDate() && currentDate <= entry.getEndDate())
                    return entry.getNb_pers();
            }
            return 0;
        }
        return 0;
    }

    public void executeVote(double value, String vote) {
        double val = value;
        int nb = checkNbPerson(System.currentTimeMillis());
        if (nb == 0)
            nb = 1;
        switch (vote) {
            case "++":
                val = val + (1.5 / nb);
                break;
            case "+":
                val = val + (0.75 / nb);
                break;
            case "-":
                val = val - (0.75 / nb);
                break;
            case "--":
                val = val - (1.5 / nb);
                break;
        }
        regulator.setConsigne(val);
    }

    private void putInArff(double cons, long timeEnd) {
        DataFromKNX df = new DataFromKNX(cons, checkNbPerson(timeEnd));
        DataLearning dl = new DataLearning(df);
        dl.setHeat_time(RepetetiveTask.timeStart, timeEnd);
        dl.setInArff();
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public Control_service getService() {
            return Control_service.this;
        }
    }
}