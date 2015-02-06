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

    public RepetetiveTask(long firstDelay){
        scheduler.scheduleWithFixedDelay(new DoSomethingTask(), firstDelay, 1440, TimeUnit.MINUTES);
    }

    private class DoSomethingTask implements Runnable{
        @Override
        public void run(){
            doSomething();
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
}
