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
import fr.esir.interfaces.OnSearchCompleted;
import fr.esir.interfaces.Prevision;
import fr.esir.objects.ArffGenerated;
import fr.esir.objects.DatesInterval;
import fr.esir.objects.WeatherForecast;

import java.io.*;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.*;
import java.util.Map.Entry;

public class DatabaseRegression implements Prevision, OnSearchCompleted {

    private WeatherForecast weather;

    private static double minTemp = 0;
    private static double maxTemp = 0;
    private static ArffGenerated arff;
    private static HashMap<Date, WeatherForecast> weatherMap = new HashMap<>();
    private static SortedMap<Time, List<DatesInterval>> datesinte;

    private List<DatesInterval> list;

    public DatabaseRegression() {
        this.weather = new WeatherForecast(this);
        this.list = null;
    }

    // public List<DatesInterval> getListData() {
    // return list;
    // }

    /**
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    public void weatherSearch() throws IOException {
        weather.executeApiForcast();
    }

    public void predict() {
        AsynchDB asynbd = new AsynchDB(this);
        asynbd.execute();
    }

    public void predictNext(ResultSet result) throws Exception {
        Log.w("Start", "predict");
        //weatherSearch();
        boolean wasInLoop = false;

        datesinte = new TreeMap<>();

        if (!weatherMap.isEmpty())
            weatherMap.clear();
        // execute the linear regression model for each student included in the
        // above list
        /*if (result != null) {
            while (result.next()) {
                wasInLoop = true;
                Time dat = result.getTime(2);
                if (!weatherMap.containsKey(dat)) {
                    // SimpleDateFormat ft = new SimpleDateFormat(
                    // "yyyy-MM-dd HH:mm:ss");

                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(dat);
                    weather.executeSearch(calendar.get(Calendar.HOUR_OF_DAY));
                    weatherMap.put(dat, weather);
                }

                arff = new ArffGenerated();
                arff.generateArff(result.getString(1));
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
                Time tm = result.getTime(2);
                // execute the model on the data
                Double tempC = arff.executeModel();
                DatesInterval dateinterv = new DatesInterval(tm, result.getTime(3),
                        verifSeuil(tempC), 0, weatherMap.get(dat));
                if (!datesinte.containsKey(tm)) {
                    datesinte.put(tm, new ArrayList<DatesInterval>());
                    datesinte.get(tm).add(dateinterv);
                } else {
                    datesinte.get(tm).add(dateinterv);
                }
            }
        }
        if (wasInLoop)*/
        Time dat = new Time(8, 0, 0);
        if (!weatherMap.containsKey(dat)) {
            // SimpleDateFormat ft = new SimpleDateFormat(
            // "yyyy-MM-dd HH:mm:ss");

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dat);
            weather.executeSearch(calendar.get(Calendar.HOUR_OF_DAY));
            weatherMap.put(dat, weather);
        }
        arff = new ArffGenerated();
        //arff.generateArff(result.getString(1));
        arff.generateArff("111");
        arff.addDataGeneric();
        Log.w("hum", weather.getHumidity() + "");
        Log.w("temp", weather.getTemp() + "");
        Log.w("hum", weather.getHumidity() + "");
        arff.addInstance(weather.getHumidity(), weather.getTemp(),
                weather.getLum());
        Time tm = new Time(8, 0, 0);
        // execute the model on the data
        Double tempC = arff.executeModel();
        Log.w("TempC", tempC + "");
        DatesInterval dateinterv = new DatesInterval(tm, new Time(10, 0, 0),
                verifSeuil(tempC), 0, weatherMap.get(dat));
        if (!datesinte.containsKey(tm)) {
            datesinte.put(tm, new ArrayList<>());
            datesinte.get(tm).add(dateinterv);
        } else {
            datesinte.get(tm).add(dateinterv);
        }

        list = calcultab(datesinte);

        if (list != null)
            Log.w("List not null", list.get(0).toString());
        else
            Log.w("List null", "The list is empty");
    }

    private List<DatesInterval> calcultab(
            SortedMap<Time, List<DatesInterval>> datesinter) {
        SortedMap<Time, List<DatesInterval>> dat = datesinter;
        List<DatesInterval> datesTemp = new ArrayList<>();
        for (Entry<Time, List<DatesInterval>> entry : dat.entrySet()) {
            int nb = entry.getValue().size();
            datesTemp.add(new DatesInterval(entry.getKey(), entry.getValue()
                    .get(0).getStartEnd(), medianCalculation(entry.getValue()),
                    nb, entry.getValue().get(0).getPrevision()));
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
            Log.w("path", file.getAbsolutePath() + "");
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

    @Override
    public void onSearchCompleted(boolean o) {
        if ((boolean) o) {
            try {
                predict();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Log.e("OEP", "No WeatherForecast");
    }

    @Override
    public void onSearchbisCompleted(ResultSet o) {
        try {
            predictNext(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}