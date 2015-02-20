package fr.esir.oep;

import com.example.esir.nsoc2014.tsen.lob.objects.ArffGenerated;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 20/02/2015.
 */
public class Weather {
    private double humidity;
    private double temp;
    private double lum;
    private WeatherForecast wf;

    public Weather(WeatherForecast wf) {
        this.humidity = 0;
        this.temp = 0;
        this.lum = 0;
        this.wf = wf;
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

    public void executeSearch(int start_hour) throws Exception {
        System.out.println("heure : " + start_hour);
        double cloudcover;
        int pos = closest(start_hour, list) / 3 + 1;
        System.out.println("pos : " + pos);
        temp = wf.getHourly().getJSONObject(pos).getDouble("tempC");
        humidity = wf.getHourly().getJSONObject(pos).getDouble("humidity");
        cloudcover = wf.getHourly().getJSONObject(pos).getDouble("cloudcover");
        System.out.println("heure : " + wf.getHourly().getJSONObject(pos).getDouble("time"));

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
        arffinou.addInstance(humidity, temp, cloudCover, wf.getSeason());
        lum = arffinou.executeModel();
    }

}
