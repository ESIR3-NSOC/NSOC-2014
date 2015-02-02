package fr.esir.oep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import fr.esir.interfaces.Prevision;
import fr.esir.objects.DatesInterval;

import java.util.List;

public class PredictBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Prevision db = new DatabaseRegression();
        try {
            List<DatesInterval> list= db.predict();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
