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
    private double consigne;

    public RepetetiveTask(long firstDelay){
            scheduler.scheduleWithFixedDelay(new DoSomethingTask(), firstDelay, 1440, TimeUnit.MINUTES);
    }

    public  RepetetiveTask(long delay, double consigne){
        this.consigne = consigne;
        scheduler.schedule(new DoOtherSomethingTask(), delay, TimeUnit.MINUTES);
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
        //look for sensors values int the context service -> put in a DataFromKNX
        //use the new object in the MachineLearning class
        //check i_temp sensor value and wait the temp is "consigne"
        //calculate the difference between the start and end dates
        //add the values to the arff file
    }
}
