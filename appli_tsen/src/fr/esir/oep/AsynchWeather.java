package fr.esir.oep;

import android.os.AsyncTask;
import fr.esir.interfaces.OnTaskCompleted;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Nicolas on 02/02/2015.
 */
public class AsynchWeather extends AsyncTask<Void, Void, String> {
    private OnTaskCompleted listener;

    public AsynchWeather(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        CookieSpecFactory csf = new CookieSpecFactory() {
            public CookieSpec newInstance(HttpParams params) {
                return new BrowserCompatSpec() {
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin)
                            throws MalformedCookieException {
                        // Oh, I am easy
                    }
                };
            }
        };
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCookieSpecs().register("easy", csf);
        client.getParams().setParameter(
                ClientPNames.COOKIE_POLICY, "easy");
        HttpHost target = new HttpHost("api.worldweatheronline.com", 80, "http");
        HttpGet getRequest = new HttpGet(
                "/free/v2/weather.ashx?q=Rennes&format=json&date="
                        + "today&cc=no&fx24=yes&key=4156d0f2412bde4b386b3a96aae11");
        HttpResponse result = null;

        try {
            result = client.execute(target, getRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null) {
            HttpEntity entity = result.getEntity();
            try {
                return EntityUtils.toString(entity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "failed";
    }

    @Override
    protected void onPostExecute(String result) {
        //Do something with result
        listener.onTaskCompleted(result);
    }
}
