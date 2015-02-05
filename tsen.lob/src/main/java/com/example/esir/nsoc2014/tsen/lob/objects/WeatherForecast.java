package com.example.esir.nsoc2014.tsen.lob.objects;

import com.example.esir.nsoc2014.tsen.lob.interfaces.OnSearchCompleted;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherForecast{
    private double humidity;
    private double temp;
    private double lum;
    private double saison;

    private OnSearchCompleted listener;

    private JSONArray hourly;

    public WeatherForecast(OnSearchCompleted listener) {
        this.humidity = 0;
        this.temp = 0;
        this.lum = 0;
        this.listener = listener;
    }

    public double getLum() {
        return lum;
    }

    /**
     * get the humidity value
     *
     * @return the humidity
     */
    public double getHumidity() {
        return humidity;

    }

    /**
     * get the temperature value
     *
     * @return the temp
     */
    public double getTemp() {
        return temp;
    }

    private ArrayList<Integer> list = new ArrayList<Integer>() {
        private static final long serialVersionUID = 1L;

        {
            add(1);
            add(4);
            add(7);
            add(10);
            add(13);
            add(16);
            add(19);
            add(22);
        }
    };

    private static final int seasons[] = {2, 2, 4, 4, 1, 1, 1, 1, 3, 3, 2, 2};

    private int getSeason(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return seasons[cal.get(Calendar.MONTH)];
    }

    public void executeSearch(int start_hour) throws Exception {
        System.out.println("heure : " + start_hour);
        double cloudcover;
        int pos = closest(start_hour, list) / 3 + 1;
        System.out.println("pos : " + pos);
        temp = hourly.getJSONObject(pos).getDouble("tempC");
        humidity = hourly.getJSONObject(pos).getDouble("humidity");
        cloudcover = hourly.getJSONObject(pos).getDouble("cloudcover");
        System.out.println("heure : " + hourly.getJSONObject(pos).getDouble("time"));

        calculLum(cloudcover);

    }

    /**
     * find the forecast time for a given time
     *
     * @param of look for this int
     * @param in in this list
     * @return the int the closest to of
     */
    private static int closest(int of, List<Integer> in) {
        int min = Integer.MAX_VALUE;
        int closest = of;

        for (int v : in) {
            final int diff = Math.abs(v - of);

            if (diff < min) {
                min = diff;
                closest = v;
            }
        }

        return closest;
    }

    private void calculLum(double cloudCover) throws Exception {
        ArffGenerated arffinou = new ArffGenerated();
        arffinou.generateArfflum();
        arffinou.addInstance(humidity, temp, cloudCover, saison);
        lum = arffinou.executeModel();
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
