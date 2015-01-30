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

package com.example.esir.nsoc2014.tsen.lob.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.http.client.ClientProtocolException;

import com.example.esir.nsoc2014.tsen.lob.arff.ArffGenerated;
import com.example.esir.nsoc2014.tsen.lob.interfaces.Prevision;
import com.example.esir.nsoc2014.tsen.lob.interfaces.ServiceConnection;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import com.example.esir.nsoc2014.tsen.lob.objects.WeatherForecast;

public class DatabaseRegression implements Prevision {

	private WeatherForecast weather;

	private static double minTemp = 0;
	private static double maxTemp = 0;
	private static ArffGenerated arff;
	private static HashMap<Date, WeatherForecast> weatherMap = new HashMap<>();
	private static SortedMap<Time, List<DatesInterval>> datesinte;

	private List<DatesInterval> list;

	// public DatabaseRegression() {
	// this.list = null;
	// }

	// public List<DatesInterval> getListData() {
	// return list;
	// }

	/**
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private void weatherSearch() throws ClientProtocolException, IOException {
		weather = new WeatherForecast();
		weather.executeApiForcast();
	}

	public List<DatesInterval> predict() throws Exception {
		weatherSearch();
		ServiceConnection jdbc = new JdbcData();
		ResultSet result = null;
		if (jdbc.connect())
			result = jdbc.getDataFromDB();
		weather.executeSearch(8);

		if (result.getFetchSize() <= 0 || result == null)
			return null;

		datesinte = new TreeMap<Time, List<DatesInterval>>();

		if (!weatherMap.isEmpty())
			weatherMap.clear();
		// execute the linear regression model for each student included in the
		// above list

		while (result.next()) {
			Date dat = result.getDate(2);
			if (!weatherMap.containsKey(dat)) {
				// SimpleDateFormat ft = new SimpleDateFormat(
				// "yyyy-MM-dd HH:mm:ss");

				System.out.println(dat);
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
		list = calcultab(datesinte);
		jdbc.close();
		return list;
	}

	/**
	 * 
	 * @param datesinter
	 */
	private List<DatesInterval> calcultab(
			SortedMap<Time, List<DatesInterval>> datesinter) {
		SortedMap<Time, List<DatesInterval>> dat = datesinter;
		List<DatesInterval> datesTemp = new ArrayList<DatesInterval>();
		for (Entry<Time, List<DatesInterval>> entry : dat.entrySet()) {
			int nb = entry.getValue().size();
			datesTemp.add(new DatesInterval(entry.getKey(), entry.getValue()
					.get(0).getStartEnd(), medianCalculation(entry.getValue()),
					nb, entry.getValue().get(0).getPrevision()));
		}
		return datesTemp;
	}

	/**
	 * 
	 * @param tab
	 * @return
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
		double median = (Double) tab.get(mid).getConsigne();
		if (tab.size() % 2 == 0) {
			median = (median + (Double) tab.get(mid - 1).getConsigne()) / 2;
		}

		return median;
	}

	private double verifSeuil(double temp) {
		findMinMax();
		return temp = temp < minTemp ? minTemp : (temp > maxTemp ? maxTemp
				: temp);
	}

	/**
	 * 
	 */
	private void findMinMax() {
		try {
			FileReader fileToRead = new FileReader("./data/confData.txt");
			BufferedReader bf = new BufferedReader(fileToRead);
			String aLine;
			String[] values = null;
			boolean okmin = false;
			boolean okmax = false;
			while ((aLine = bf.readLine()) != null) {
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
			System.out.println(e.getMessage());
		}
	}
}
