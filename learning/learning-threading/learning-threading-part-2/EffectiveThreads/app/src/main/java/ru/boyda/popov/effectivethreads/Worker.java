package ru.boyda.popov.effectivethreads;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

public class Worker extends Thread {

    private final static int HIDE_PROGRESS_BAR = 0;
    private final static int SHOW_PROGRESS_BAR = 1;
    private final static int RANDOM_NUMBER_OF_WAITING = 5000;

    private Handler workerHandler;
    private Handler responseHandler;

    public Worker(Handler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public void run() {
        Looper.prepare();
        workerHandler = new Handler();
        Looper.loop();
    }

    public void doWork() {
        workerHandler.post(new Runnable() {
            @Override
            public void run() {
                Message firstMessage = responseHandler.obtainMessage(SHOW_PROGRESS_BAR, 0, 0, null);
                firstMessage.sendToTarget();
                int random = (int) (Math.random() * RANDOM_NUMBER_OF_WAITING);
                SystemClock.sleep(random);
                Message secondMessage = responseHandler.obtainMessage(HIDE_PROGRESS_BAR, random, 0, null);
                secondMessage.sendToTarget();
            }
        });
    }

    public void exit() {
        workerHandler.getLooper().quit();
    }

}
