package fr.esir.oep;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import fr.esir.maintasks.ConfigParams;
import fr.esir.maintasks.MyActivity;
import fr.esir.regulation.DataFromKNX;
import fr.esir.regulation.MachineLearning;
import fr.esir.services.Regulation_service;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nicolas on 06/02/2015.
 */
public class RepetetiveTask {
    public final static String HEATTIME = "look_for_heatTime";
    public final static String FINALCONS = "final_temp";
    public static boolean checking = false;
    public static long timeStart;
    public static double consigne = 0;
    MyActivity ctx = (MyActivity) MyActivity.ct;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public RepetetiveTask(long firstDelay) {
        scheduler.scheduleWithFixedDelay(new DoSomethingTask(), firstDelay, 86400000, TimeUnit.MILLISECONDS);
    }

    public RepetetiveTask() {
        scheduler.scheduleAtFixedRate(new ConnectToPage(), 0, 1, TimeUnit.MINUTES);
    }

    public RepetetiveTask(long firstDelay, double consigne, String action) {
        if (action.equals(HEATTIME))
            scheduler.schedule(new CalculatedHeatTime(consigne), firstDelay, TimeUnit.MILLISECONDS);
        else if (action.equals(FINALCONS))
            scheduler.schedule(new DoFinalSomethingTask(consigne), firstDelay, TimeUnit.MILLISECONDS);
    }

    public RepetetiveTask(long delay, double consigne, double nb_pers, Date date) {
        long time = date.getTime();
        scheduler.schedule(new DoOtherSomethingTask(consigne, nb_pers, time), delay, TimeUnit.MINUTES);
    }

    private void setFinalRegul(double consigne) {
        //regulator -> sleep mode temperature
        scheduler.shutdown();
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    private void doSomething() {
        try {
            MyActivity.mOep_service.startPrediction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doOtherSomething(double consigne, double nb_pers, long time) {
        //look for sensors values int the context service -> put in a DataFromKNX
        //use the new object in the MachineLearning class
        DataFromKNX dfk = new DataFromKNX(consigne, nb_pers);
        dfk.setAll();
        MachineLearning ml = new MachineLearning(dfk);
        long heatTime = ml.setDataInArff();
        long diff = time - heatTime;
        new RepetetiveTask(diff, consigne, HEATTIME);

        String s1 = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        );

        String s2 = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(diff),
                TimeUnit.MILLISECONDS.toSeconds(diff) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
        );

        updateTvProg("Next Programming at " + s2 + " to have " + consigne + " at " + time);
        scheduler.shutdown();
    }

    private void updateTvProg(String string) {
        ctx.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ctx.setTvProg(string);
            }
        });
    }

    private void calculatedTrueHeatTime(double consigneTemp) {
        //send cons value to regulator when it's the estimated time
        //check i_temp sensor value and wait the temp is "consigne"
        //calculate the difference between the start and end dates -> DataLearning
        //add the values to the arff file
        Regulation_service.regulator.setConsigne(consigneTemp);
        checking = true;
        consigne = consigneTemp;
        timeStart = System.currentTimeMillis();
        scheduler.shutdown();
    }

    public void searchJson(String result) {
        // parsing JSON
        try {
            // JSON Object
            ObjectMapper mapper = new ObjectMapper();

            JsonNode tab = mapper.readTree(result);

            for (JsonNode data : tab) {

                String user = data.get("user").asText();
                String vote = data.get("rate").asText();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = ft.parse(data.get("date").asText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long ts = date.getTime();

                MyActivity.mContext_service.getContext().setVote(user, vote, ts);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class DoSomethingTask implements Runnable {
        @Override
        public void run() {
            doSomething();
        }
    }

    private class DoFinalSomethingTask implements Runnable {
        private double consigne;

        public DoFinalSomethingTask(double consigne) {
            this.consigne = consigne;
        }

        @Override
        public void run() {
            setFinalRegul(consigne);
        }
    }

    private class DoOtherSomethingTask implements Runnable {
        private double consigne;
        private long time;
        private double nb_pers;

        public DoOtherSomethingTask(double consigne, double nb_pers, long time) {
            this.consigne = consigne;
            this.time = time;
            this.nb_pers = nb_pers;
        }

        @Override
        public void run() {
            doOtherSomething(consigne, nb_pers, time);
        }
    }

    private class CalculatedHeatTime implements Runnable {
        private double consigne;

        public CalculatedHeatTime(double consigne) {
            this.consigne = consigne;
        }

        @Override
        public void run() {
            calculatedTrueHeatTime(consigne);
        }
    }

    private class ConnectToPage implements Runnable {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpHost target = new HttpHost("tsen.uion.fr", 80, "http");
        SharedPreferences pref = ConfigParams.context.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
        HttpGet getRequest = new HttpGet("/tsen/vote?roomId=" + pref.getString("IDROOM", "1005") + "&delay=60000");
        HttpResponse result = null;

        @Override
        public void run() {
            try {
                result = client.execute(target, getRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result != null) {
                HttpEntity entity = result.getEntity();
                try {
                    String ent = EntityUtils.toString(entity);
                    searchJson(ent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                Log.w("repetJson", "Failed");
        }
    }
}