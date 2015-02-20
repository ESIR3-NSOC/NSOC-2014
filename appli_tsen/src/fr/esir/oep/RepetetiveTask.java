package fr.esir.oep;

import android.content.Context;
import android.util.Log;
import fr.esir.regulation.DataFromKNX;
import fr.esir.maintasks.MyActivity;
import fr.esir.regulation.MachineLearning;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nicolas on 06/02/2015.
 */
public class RepetetiveTask {
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    MyActivity ctx = (MyActivity) MyActivity.ct;

    public RepetetiveTask(long firstDelay) {
        scheduler.scheduleWithFixedDelay(new DoSomethingTask(), firstDelay, 86400000, TimeUnit.MILLISECONDS);
    }

    public RepetetiveTask(long firstDelay, double consigne) {
        scheduler.schedule(new CalculatedHeatTime(consigne), firstDelay, TimeUnit.MILLISECONDS);
    }

    public RepetetiveTask(long delay, double consigne, double nb_pers, Date date) {
        long time = date.getTime();
        Log.w("TIME", time + "");
        scheduler.schedule(new DoOtherSomethingTask(consigne, nb_pers, time), delay, TimeUnit.MINUTES);
    }

    private class DoSomethingTask implements Runnable {
        @Override
        public void run() {
            doSomething();
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
        new RepetetiveTask(diff, consigne);

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

    private void calculatedTrueHeatTime(double consigne) {
        //send cons value to regulator when it's the estimated time
        //check i_temp sensor value and wait the temp is "consigne"
        //calculate the difference between the start and end dates -> DataLearning
        //add the values to the arff file


        scheduler.shutdown();
    }
}
