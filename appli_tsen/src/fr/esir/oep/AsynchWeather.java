package fr.esir.oep;

import android.os.AsyncTask;
import android.util.Log;
import com.example.esir.nsoc2014.tsen.lob.interfaces.OnSearchCompleted;
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

public class AsynchWeather extends AsyncTask<Void, Void, String> {
    private OnSearchCompleted listener;

    public AsynchWeather(OnSearchCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.w("oep", "onsearchcomleted do");
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
        }else
            Log.w("oep", "Failed");
        return "failed";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.w("oep", "onsearchcomleted post execute");
        //Do something with result
        listener.onSearchCompleted(result);
    }
}
