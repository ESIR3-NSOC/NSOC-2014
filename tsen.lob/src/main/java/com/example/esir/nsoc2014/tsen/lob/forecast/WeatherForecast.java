package com.example.esir.nsoc2014.tsen.lob.forecast;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherForecast {
	private double humidity;
	private double temp;
	private int time;

	public WeatherForecast() {
		this.humidity = 0;
		this.temp = 0;
		this.time = 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getHumidity(){
		return humidity;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getTemp(){
		return temp;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTime(){
		return time;
	}
	
	/**
	 * 
	 * @param start_hour
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void executeSearch(int start_hour) throws ClientProtocolException,
			IOException {
		// set config to ignore cookies
		RequestConfig globalConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.BEST_MATCH).build();
		CloseableHttpClient client = HttpClients.custom()
				.setDefaultRequestConfig(globalConfig).build();
		RequestConfig localConfig = RequestConfig.copy(globalConfig)
				.setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

		// specify the host, protocol, and port
		HttpHost target = new HttpHost("api.worldweatheronline.com", 80, "http");

		// specify the get request
		HttpGet getRequest = new HttpGet(
				"/free/v2/weather.ashx?q=Rennes&format=json&date="
						+ "today&cc=no&fx24=yes&key=4156d0f2412bde4b386b3a96aae11");

		getRequest.setConfig(localConfig);
		CloseableHttpResponse response = client.execute(target, getRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
		}
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String retSrc = EntityUtils.toString(entity);
				System.out.println(retSrc);
				// parsing JSON
				JSONObject result = new JSONObject(retSrc); // Convert String to
															// JSON Object
				JSONObject data = result.getJSONObject("data");
				JSONArray weather = data.getJSONArray("weather");
				JSONArray hourly = weather.getJSONObject(0).getJSONArray(
						"hourly");
				System.out.println(hourly.getJSONObject(0).getDouble("tempC"));
				System.out.println(hourly.getJSONObject(0)
						.getDouble("humidity"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param of
	 * @param in
	 * @return
	 */
	public int closest(int of, List<Integer> in) {
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
}
