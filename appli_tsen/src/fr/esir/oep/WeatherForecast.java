package fr.esir.oep;

import com.example.esir.nsoc2014.tsen.lob.interfaces.OnSearchCompleted;
import com.example.esir.nsoc2014.tsen.lob.objects.ArffGenerated;
import fr.esir.maintasks.MyActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherForecast{
    private double saison;

    private OnSearchCompleted listener;

    private JSONArray hourly;


    public WeatherForecast(OnSearchCompleted listener){
        this.hourly = null;
        this.saison = -1;
        this.listener = listener;
    }

    private static final int seasons[] = {2, 2, 4, 4, 1, 1, 1, 1, 3, 3, 2, 2};

    private int getSeason(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return seasons[cal.get(Calendar.MONTH)];
    }

    public JSONArray getHourly(){
        return hourly;
    }

    public double getSeason(){
        return saison;
    }

    public void searchDone(String weath){
        // parsing JSON
        JSONObject result;
        try {
            result = new JSONObject(weath);
            // JSON Object
            JSONObject data = result.getJSONObject("data");
            JSONArray weather = data.getJSONArray("weather");
            hourly = weather.getJSONObject(0).getJSONArray("hourly");
            Date datenow = new Date();
            saison = getSeason(datenow);
            listener.onSearchCompleted(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
