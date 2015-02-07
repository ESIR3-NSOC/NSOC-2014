package fr.esir.oep;

import fr.esir.maintasks.MyActivity;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nicolas on 06/02/2015.
 */
public class RepetetiveTask {
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static int ACTION_PREDICT = 1;
    public static int ACTION_REGUL = 2;

    public RepetetiveTask(long firstDelay,int action){
        if(action == ACTION_PREDICT)
            scheduler.scheduleWithFixedDelay(new DoSomethingTask(), firstDelay, 1440, TimeUnit.MINUTES);
        else if(action == ACTION_REGUL)
            scheduler.scheduleWithFixedDelay(new DoOtherSomethingTask(), firstDelay, 1440, TimeUnit.MINUTES);
    }

    private class DoSomethingTask implements Runnable{
        @Override
        public void run(){
            doSomething();
        }
    }

    private class DoOtherSomethingTask implements Runnable{
        @Override
        public void run(){
            doOtherSomething();
        }
    }

    public ScheduledExecutorService getScheduler(){
        return scheduler;
    }

    private void doSomething(){
        try {
            MyActivity.mOep_service.startPrediction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doOtherSomething(){
        //look for sensors values -> put in a DataFromKNX
        //use the new object in the MachineLearning class
        //check i_temp sensor value and wait the temp is "consigne"
        //calculate the difference between the start and end dates
        //add the values to the arff file
    }
}
