package com.example.esir.nsoc2014.tsen.lob.forecast;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class WeatherForecast {

	public static void main(String[] args) throws ClientProtocolException,
			IOException {

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
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null)
				System.out.println(response.getStatusLine().toString());
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
}
