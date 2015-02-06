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

import android.os.Environment;
import android.util.Log;
import com.example.esir.nsoc2014.tsen.lob.interfaces.OnSearchCompleted;
import com.example.esir.nsoc2014.tsen.lob.interfaces.Prevision;
import com.example.esir.nsoc2014.tsen.lob.objects.ArffGenerated;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import com.example.esir.nsoc2014.tsen.lob.objects.WeatherForecast;

import java.io.*;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.*;
import java.util.Map.Entry;

public class DatabaseRegression implements Prevision {

    private WeatherForecast weather;
    private double minTemp = 0;
    private double maxTemp = 0;
    private HashMap<Time, List<DatesInterval>> datesinte;
    private List<DatesInterval> list;

    private OnSearchCompleted listener;

    public DatabaseRegression(OnSearchCompleted os) {
        this.weather = new WeatherForecast(os);
        this.list = null;
        this.datesinte = null;
        this.listener = os;
    }

    public List<DatesInterval> getList() {
        return list;
    }

    public HashMap<Time, List<DatesInterval>> getHashmap() {
        return datesinte;
    }

    public WeatherForecast getWeatherForecast() {
        return weather;
    }

    // public List<DatesInterval> getListData() {
    // return list;
    // }


    public void predictNext(ResultSet result) throws Exception {
        Log.w("Start", "predict");
        //weatherSearch();
        boolean wasInLoop = false;
        HashMap<Date, WeatherForecast> weatherMap = new HashMap<>();
        datesinte = new HashMap<>();

        if (!weatherMap.isEmpty())
            weatherMap.clear();
        // execute the linear regression model for each student included in the
        // above list
        if (result != null) {
            while (result.next()) {
                wasInLoop = true;
                Time dat = result.getTime(2);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(dat);
                if (!weatherMap.containsKey(dat)) {
                    weather.executeSearch(calendar.get(Calendar.HOUR_OF_DAY));
                    weatherMap.put(calendar.getTime(), weather);
                }

                ArffGenerated arff = new ArffGenerated();
                arff.generateArff(result.getString(1));

                //get information from context -> addDataCustom

                //if a student has not enough data (vote)
                arff.addDataGeneric();

                // int id = result.getInt(1); // get the id of the student
                // System.out.println("id= " + id);
                // query.setQuery("SELECT * FROM preference_student WHERE student_id="
                // + id); // mysql command
                // Instances datapref = query.retrieveInstances(); // database to
                // arff
                // // format
                // if (datapref.isEmpty()) {
                // //JdbcData.connexionBase(id.toString());
                // query.setQuery("SELECT * FROM preference_student WHERE student_id="
                // + id);
                // datapref = query.retrieveInstances();
                // }
                //
                // // filter studen_id
                // Remove remove = new Remove();
                // remove.setAttributeIndices("1");
                // remove.setInvertSelection(false);
                // remove.setInputFormat(datapref);
                // Instances instNew = Filter.useFilter(datapref, remove);
                //
                // // System.out.println(instNew);
                //
                arff.addInstance(weather.getHumidity(), weather.getTemp(),
                        weather.getLum());
                // execute the model on the data
                Double tempC = arff.executeModel();
                DatesInterval dateinterv = new DatesInterval(result.getString(1), dat, result.getTime(3),
                        verifSeuil(tempC), weatherMap.get(calendar.getTime()).getTemp(), weatherMap.get(calendar.getTime()).getLum(), weatherMap.get(calendar.getTime()).getHumidity(), result.getString(4));

                if (!datesinte.containsKey(dat)) {
                    Log.w("tm", dat + "");
                    datesinte.put(dat, new ArrayList<>());
                    datesinte.get(dat).add(dateinterv);
                } else {
                    datesinte.get(dat).add(dateinterv);
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
            datesTemp.add(new DatesInterval(entry.getKey(), entry.getValue()
                    .get(0).getStartEnd(), medianCalculation(entry.getValue()),
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
        findMinMax();
        return temp < minTemp ? minTemp : (temp > maxTemp ? maxTemp
                : temp);
    }

    private void createMyFile(String content) {
        try {
            String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
            File myFile = new File(externalStorage + File.separator + "DCIM", "tsen_confData.txt");

            FileWriter fileWriter = new FileWriter(myFile);
            fileWriter.append(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void findMinMax() {
        try {
            String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file = new File(externalStorage + File.separator + "DCIM" + File.separator + "tsen_confData.txt");
            //File file = new File("data/tsen_confData.txt");
            if (!file.exists()) {
                String content = "ADE_ID:1005\n" +
                        "SALLE:104\n" +
                        "tres chaud:-1\n" +
                        "chaud:-0.5\n" +
                        "bon:0\n" +
                        "froid:0.5\n" +
                        "tres froid:1\n" +
                        "minValue:20\n" +
                        "maxValue:24";
                createMyFile(content);
            }
            //Log.w("path", file.getAbsolutePath() + "");
            FileReader fileToRead = new FileReader(file);
            BufferedReader bf = new BufferedReader(fileToRead);
            String aLine;
            String[] values;
            boolean okmin = false;
            boolean okmax = false;
            while ((aLine = bf.readLine()) != null || (!okmin && !okmax)) {
                if (aLine.startsWith("minValue")) {
                    values = aLine.split(":");
                    minTemp = Double.parseDouble(values[1]);

                    okmin = true;
                } else if (aLine.startsWith("maxValue")) {
                    values = aLine.split(":");
                    maxTemp = Double.parseDouble(values[1]);
                    okmax = true;
                }
            }
            if (!okmin)
                minTemp = 20;
            if (!okmax)
                maxTemp = 24;
            bf.close();
        } catch (IOException e) {
            //nothing happened
        }
    }
}
