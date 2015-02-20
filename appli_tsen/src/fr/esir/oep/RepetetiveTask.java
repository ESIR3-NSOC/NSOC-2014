package fr.esir.oep;

import android.content.Context;
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
    private double consigne;
    private long time;

    public static String ACTION_PREDICT = "schedule_prediction";
    public static String ACTION_REGULATION = "schedule_heating";

    public RepetetiveTask(long firstDelay, String action) {
        if (action.equals(ACTION_PREDICT))
            scheduler.scheduleWithFixedDelay(new DoSomethingTask(), firstDelay, 86400000, TimeUnit.MILLISECONDS);
        else if (action.equals(ACTION_REGULATION))
            scheduler.schedule(new CalculatedHeatTime(), firstDelay, TimeUnit.MILLISECONDS);
    }

    public RepetetiveTask(long delay, double consigne, Date date) {
        this.consigne = consigne;
        time = date.getTime();
        scheduler.schedule(new DoOtherSomethingTask(), delay, TimeUnit.MINUTES);
    }

    private class DoSomethingTask implements Runnable {
        @Override
        public void run() {
            doSomething();
        }
    }

    private class DoOtherSomethingTask implements Runnable {
        @Override
        public void run() {
            doOtherSomething();
        }
    }

    private class CalculatedHeatTime implements Runnable {
        @Override
        public void run() {
            calculatedTrueHeatTime();
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

    private void doOtherSomething() {
        //look for sensors values int the context service -> put in a DataFromKNX
        //use the new object in the MachineLearning class
        DataFromKNX dfk = new DataFromKNX(consigne);
        dfk.setAll();
        MachineLearning ml = new MachineLearning(dfk);
        long heatTime = ml.setDataInArff();
        long diff = time - heatTime;
        new RepetetiveTask(diff, ACTION_REGULATION);

        scheduler.shutdown();
    }


    private void calculatedTrueHeatTime() {
        //send cons value to regulator when it's the estimated time
        //check i_temp sensor value and wait the temp is "consigne"
        //calculate the difference between the start and end dates -> DataLearning
        //add the values to the arff file
        scheduler.shutdown();
    }
}
