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

    public static String ACTION_PREDICT = "schedule_prediction";
    public static String ACTION_REGULATION = "schedule_heating";

    public RepetetiveTask(long firstDelay) {
        scheduler.scheduleWithFixedDelay(new DoSomethingTask(), firstDelay, 86400000, TimeUnit.MILLISECONDS);
    }

    public RepetetiveTask(long firstDelay, double consigne){
        scheduler.schedule(new CalculatedHeatTime(consigne), firstDelay, TimeUnit.MILLISECONDS);
    }

    public RepetetiveTask(long delay, double consigne, Date date) {
        long time = date.getTime();
        Log.w("TIME", time+"");
        scheduler.schedule(new DoOtherSomethingTask(consigne, time), delay, TimeUnit.MINUTES);
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
        public DoOtherSomethingTask(double consigne, long time){
            this.consigne = consigne;
            this.time = time;
        }
        @Override
        public void run() {
            doOtherSomething(consigne,time);
        }
    }

    private class CalculatedHeatTime implements Runnable {
        private double consigne;
        public CalculatedHeatTime(double consigne){
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

    private void doOtherSomething(double consigne, long time) {
        //look for sensors values int the context service -> put in a DataFromKNX
        //use the new object in the MachineLearning class
        DataFromKNX dfk = new DataFromKNX(consigne);
        dfk.setAll();
        MachineLearning ml = new MachineLearning(dfk);
        long heatTime = ml.setDataInArff();
        long diff = time - heatTime;
        new RepetetiveTask(diff,consigne);
        scheduler.shutdown();
    }

    private void calculatedTrueHeatTime(double consigne) {
        //send cons value to regulator when it's the estimated time
        //check i_temp sensor value and wait the temp is "consigne"
        //calculate the difference between the start and end dates -> DataLearning
        //add the values to the arff file


        scheduler.shutdown();
    }
}
