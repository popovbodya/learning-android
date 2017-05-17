package ru.bodya.popov.learning_threading_part_1.basics;


import android.os.Handler;
import android.os.HandlerThread;

class MyWorkerThread extends HandlerThread {

    private Handler mWorkerHandler;

    MyWorkerThread(String name) {
        super(name);
    }

    void postTask(Runnable task) {
        mWorkerHandler.post(task);
    }

    @Override
    protected void onLooperPrepared() {
        mWorkerHandler = new Handler(getLooper());
    }

}