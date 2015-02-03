package fr.esir.oep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import fr.esir.interfaces.Prevision;

import java.io.IOException;

public class PredictBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Prevision db = new DatabaseRegression();
        //List<DatesInterval> list = null;
        try {
            db.weatherSearch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*if (list != null)
            Log.w("List not null", list.get(0).toString());
        else
            Log.w("List null", "The list is empty");*/
    }
}
