package bodya.sbt.ru.currentwork.async;


import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bodya.sbt.ru.currentwork.Animal;
import bodya.sbt.ru.currentwork.AnimalStorage;
import bodya.sbt.ru.currentwork.interfaces.OnAnimalContentChangeListener;

public class DataBaseWorker extends HandlerThread implements OnAnimalContentChangeListener {

    private static final String ANIMAL_KEY = "animal_key";
    private static final String TAG = "DataBaseWorker";

    private Handler workerHandler;
    private AnimalStorage storage;
    private List<Animal> cachedData;
    private WeakReference<LoaderCallback> callbackWeakReference = new WeakReference<>(null);


    public interface LoaderCallback {
        void onLoadFinished(List<Animal> animalList);
    }

    public synchronized void setListener(LoaderCallback callback) {
        callbackWeakReference = new WeakReference<>(callback);
    }

    public DataBaseWorker(AnimalStorage storage) {
        super(TAG);
        this.storage = storage;
    }

    public void queueTask(final int what) {
        Log.e(TAG, "task added to the queue");
        workerHandler.obtainMessage(what).sendToTarget();
    }

    public void queueTask(final int what, Animal animal) {
        Message message = workerHandler.obtainMessage(what);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ANIMAL_KEY, animal);
        message.setData(bundle);
        message.sendToTarget();
    }

    @Override
    public void onContentChanged() {
        Log.e(TAG, "onContentChanged");

        cachedData = new ArrayList<>(storage.getAnimalList());
        loadAnimals();

    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        prepareHandler();
        storage.addOnContentChangeListener(this);
    }


    @Override
    public boolean quit() {
        storage.removeOnContentChangeListener(this);
        return super.quit();
    }

    private void prepareHandler() {
        workerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.e(TAG, "handleMessage");
                switch (msg.what) {
                    case DataBaseLoaderFunctions.READ_ANIMALS: {
                        if (cachedData == null) {
                            cachedData = new ArrayList<>(storage.getAnimalList());
                            loadAnimals();
                        } else {
                            cachedData = new ArrayList<>(storage.getAnimalList());
                        }
                        return true;

                    }
                    case DataBaseLoaderFunctions.DELETE_ANIMAL: {
                        storage.deleteAnimal((Animal) msg.getData().getSerializable(ANIMAL_KEY));
                        return true;
                    }
                    case DataBaseLoaderFunctions.UPDATE_ANIMAL: {
                        storage.updateAnimal((Animal) msg.getData().getSerializable(ANIMAL_KEY));
                        return true;
                    }
                    case DataBaseLoaderFunctions.CREATE_ANIMAL: {
                        storage.addAnimal((Animal) msg.getData().getSerializable(ANIMAL_KEY));
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }

    private void loadAnimals() {
        Log.e(TAG, "loadAnimals with thread" + Thread.currentThread().toString());
        synchronized (this) {
            final LoaderCallback loaderCallback = callbackWeakReference.get();
            if (loaderCallback != null) {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loaderCallback.onLoadFinished(cachedData);
                    }
                });
            }
        }
    }

}