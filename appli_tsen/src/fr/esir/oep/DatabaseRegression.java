/*
 * Use a linear regression model on data from preferences database (users)
 * 
 * Inputs : predicted weather {temperature out, humidity out}, schedule of the day {hours, class}
 * DB must be completed : class -> list of users (or number which can identify a user)
 * users -> preferences (temp out, humidity out, temp in (consign))
 * Outputs : schedule of the day {temperature in consign, hours}
 * 
 * Tested locally for now
 * 
 */

package fr.esir.oep;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.esir.nsoc2014.tsen.lob.interfaces.OnSearchCompleted;
import com.example.esir.nsoc2014.tsen.lob.interfaces.Prevision;
import com.example.esir.nsoc2014.tsen.lob.objects.ArffGenerated;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.maintasks.ConfigParams;
import fr.esir.maintasks.MyActivity;

import java.sql.ResultSet;
import java.sql.Time;
import java.util.*;
import java.util.Map.Entry;

public class DatabaseRegression implements Prevision {

    private HashMap<Time, List<DatesInterval>> datesinte;
    private List<DatesInterval> list;
    private WeatherForecast wf;
    private OnSearchCompleted listener;

    public DatabaseRegression(WeatherForecast wf, OnSearchCompleted listener) {
        this.wf = wf;
        this.list = null;
        this.datesinte = null;
        this.listener = listener;
    }

    public List<DatesInterval> getList() {
        return list;
    }

    public HashMap<Time, List<DatesInterval>> getHashmap() {
        return datesinte;
    }


    public void predictNext(ResultSet result) throws Exception {
        Log.w("Start", "predict");
        //weatherSearch();
        boolean wasInLoop = false;
        HashMap<Time, Weather> weatherMap = new HashMap<>();
        datesinte = new HashMap<>();

        if (!weatherMap.isEmpty())
            weatherMap.clear();
        // execute the linear regression model for each student included in the
        // above list
        if (result != null) {
            while (result.next()) {
                Log.i("INFO", result.getString(1) + " " + result.getTime(2) + " " + result.getTime(3) + " " + result.getString(4));

                wasInLoop = true;
                Time dat = result.getTime(2);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(dat);
                Log.i("dat", dat + "");
                if (!weatherMap.containsKey(dat)) {
                    Weather weather = new Weather(wf);
                    weather.executeSearch(calendar.get(Calendar.HOUR_OF_DAY));
                    weatherMap.put(dat, weather);
                }

                String user = result.getString(1);

                ArffGenerated arff = new ArffGenerated();
                arff.generateArff(result.getString(1));

                //get information from context -> addDataCustom

                //if a student has not enough data (vote)
                arff.addDataGeneric();

                arff.addInstance(weatherMap.get(dat).getHumidity(), weatherMap.get(dat).getTemp(),
                        weatherMap.get(dat).getLum());
                Log.w("weather", "lum " + weatherMap.get(dat).getLum() + " hum " + weatherMap.get(dat).getHumidity() + " temp " + weatherMap.get(dat).getTemp());

                // execute the model on the data
                Double tempC = arff.executeModel();
                //user,start time, end time, cons, temp ext, lum ext, hum ext, lesson
                if (!datesinte.containsKey(dat)) {
                    Log.w("tm", dat + "");
                    datesinte.put(dat, new ArrayList<>());
                    datesinte.get(dat).add(new DatesInterval(user, dat, result.getTime(3),
                            verifSeuil(tempC), weatherMap.get(dat).getTemp(),
                            weatherMap.get(dat).getLum(), weatherMap.get(dat).getHumidity(), result.getString(4)));
                } else {
                    datesinte.get(dat).add(new DatesInterval(user, dat, result.getTime(3),
                            verifSeuil(tempC), weatherMap.get(dat).getTemp(),
                            weatherMap.get(dat).getLum(), weatherMap.get(dat).getHumidity(), result.getString(4)));
                }
            }
        }

        if (wasInLoop) {
            list = calcultab(datesinte);
            listener.onSearchCompleted();
        }


        if (list != null) {
            Log.w("List not null", "List not null");
        } else
            Log.w("List null", "The list is empty");
    }

    private List<DatesInterval> calcultab(
            HashMap<Time, List<DatesInterval>> datesinter) {
        List<DatesInterval> datesTemp = new ArrayList<>();
        for (Entry<Time, List<DatesInterval>> entry : datesinter.entrySet()) {
            int nb = entry.getValue().size();
            Log.i("ENTRYDATE", entry.getKey() + "");
            datesTemp.add(new DatesInterval(entry.getKey(), entry.getValue()
                    .get(0).getEndDate(), medianCalculation(entry.getValue()),
                    nb, entry.getValue().get(0).getTemp(), entry.getValue().get(0).getlum(), entry.getValue().get(0).gethumidity(), entry.getValue()
                    .get(0).getLesson()));
        }
        return datesTemp;
    }

    /**
     * @param tab tab
     * @return double
     */
    private double medianCalculation(List<DatesInterval> tab) {
        Collections.sort(tab, new Comparator<DatesInterval>() {
            @Override
            public int compare(DatesInterval arg0, DatesInterval arg1) {
                // TODO Auto-generated method stub
                return arg0.compareTo(arg1);
            }
        });

        int mid = tab.size() / 2;
        double median = tab.get(mid).getConsigne();
        if (tab.size() % 2 == 0) {
            median = (median + tab.get(mid - 1).getConsigne()) / 2;
        }

        return median;
    }

    private double verifSeuil(double temp) {
        Log.w("Cons", temp + "");
        SharedPreferences sh = ConfigParams.context.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
        Double minTemp = Double.parseDouble(sh.getString("TEMPMIN", "20"));
        Double maxTemp = Double.parseDouble(sh.getString("TEMPMAX", "25"));
        return temp < minTemp ? minTemp : (temp > maxTemp ? maxTemp
                : temp);
    }
}
