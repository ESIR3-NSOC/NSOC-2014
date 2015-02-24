package fr.esir.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.example.esir.nsoc2014.tsen.lob.interfaces.OnSearchCompleted;
import com.example.esir.nsoc2014.tsen.lob.interfaces.Prevision;
import com.example.esir.nsoc2014.tsen.lob.interfaces.Service_oep;
import fr.esir.knx.Reference;
import fr.esir.maintasks.ConfigParams;
import fr.esir.oep.*;
import fr.esir.resources.FilterString;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;

public class Oep_service extends Service implements OnSearchCompleted, Service_oep {
    private final IBinder mBinder = new LocalBinder();
    public WeatherForecast wf;
    //private AlarmManager am;
    private RepetetiveTask rt;
    private Context context = ConfigParams.context;
    private Prevision db;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //am = null;
        rt.getScheduler().shutdown();
        rt = null;
        return super.onUnbind(intent);
    }

    public boolean initialize() {
        SharedPreferences sh = context.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
        //oep alarm manager
        //create new calendar instance
        Log.w("predictinitalarm", "ok");
        Log.w("DELAY", sh.getLong("DELAY", Reference.timeBefore()) + "");
        rt = new RepetetiveTask(sh.getLong("DELAY", Reference.timeBefore()));

        wf = new WeatherForecast(this);
        try {
            weatherSearch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, String key, Bundle data) {
        final Intent intent = new Intent(action);
        intent.putExtra(key, data);
        sendBroadcast(intent);
    }

    public void weatherSearch() throws IOException {
        Log.w("weathersearch", "OK");
        AsynchWeather task = new AsynchWeather(this);
        // System.out.println(retSrc);
        task.execute();
    }

    public void predict() {
        AsynchDB asynbd = new AsynchDB(this);
        asynbd.execute();
    }

    @Override
    public void onSearchCompleted(boolean o) {
        if (o) {
            try {
                Log.w("oep", "onsearchcomleted boolean");
                predict();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Log.e("OEP", "No WeatherForecast");
    }

    @Override
    public void onSearchCompleted(ResultSet o) {
        try {
            db = new DatabaseRegression(wf, this);
            db.predictNext(o);
            Bundle extras = new Bundle();
            extras.putSerializable("HashMap", db.getHashmap());
            broadcastUpdate(FilterString.OEP_DATA_STUDENTS_OF_DAY, "Data", extras);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSearchCompleted(String weath) {
        Log.w("oep", "onsearchcomleted String");
        wf.searchDone(weath);
    }

    @Override
    public void onSearchCompleted() {
        Log.w("oep", "onsearchcomleted");
        Bundle extras = new Bundle();
        extras.putSerializable("List", (Serializable) db.getList());
        broadcastUpdate(FilterString.OEP_DATA_CONSIGNES_OF_DAY, "Data", extras);
    }

    public void startPrediction() throws IOException {
        //db = new DatabaseRegression(this);
        wf = new WeatherForecast(this);
        weatherSearch();
    }

    public class LocalBinder extends Binder {
        public Oep_service getService() {
            return Oep_service.this;
        }
    }
}
