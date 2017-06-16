package ru.popov.bodya.eventsmanager.db;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ru.popov.bodya.eventsmanager.Event;
import ru.popov.bodya.eventsmanager.EventStorage;
import ru.popov.bodya.eventsmanager.interfaces.OnEventContentChangeListener;

public class DataBaseWorker extends HandlerThread implements OnEventContentChangeListener {

    private static final String TAG = "DataBaseWorker";

    private Handler workerHandler;
    private List<Event> cachedData;
    private EventStorage storage;
    private WeakReference<LoaderCallback> callbackWeakReference = new WeakReference<>(null);


    public interface LoaderCallback {
        void onLoadFinished(List<Event> animalList);
    }

    public void setListener(LoaderCallback callback) {
        callbackWeakReference = new WeakReference<>(callback);
    }

    public DataBaseWorker(EventStorage storage) {
        super(TAG);
        this.storage = storage;
    }

    public void queueTask(Runnable runnable) {
        Log.e(TAG, "runnalbe added to the queue");
        workerHandler.post(runnable);
    }


    @Override
    public void onContentChanged() {
        Log.e(TAG, "onContentChanged");
        cachedData = new ArrayList<>(storage.getEventList());
        loadEventsToUi();

    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        workerHandler = new Handler();
        storage.addOnContentChangeListener(this);
    }


    @Override
    public boolean quit() {
        storage.removeOnContentChangeListener(this);
        return super.quit();
    }

    private void loadEventsToUi() {
        Log.e(TAG, "loadEventsToUi with thread" + Thread.currentThread().toString());

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                LoaderCallback loaderCallback = callbackWeakReference.get();
                if (loaderCallback != null) {
                    loaderCallback.onLoadFinished(cachedData);
                }
            }
        });

    }
}


