package unused.ru.popov.bodya;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.List;

import bodya.sbt.ru.currentwork.Animal;
import bodya.sbt.ru.currentwork.AnimalStorage;
import bodya.sbt.ru.currentwork.activities.AnimalInfoActivity;
import bodya.sbt.ru.currentwork.MyApplication;
import bodya.sbt.ru.currentwork.interfaces.AnimalsStorageProvider;

public class Worker extends HandlerThread {

    private static final String TAG = Worker.class.getSimpleName();
    private static final int DOWNLOAD_INFO = 0;
    private final AnimalStorage animalStorage;

    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private Callback mCallback;

    public interface Callback {
        void onAnimalDownloaded(Animal animal);
    }

    public Worker(Handler responseHandler, Callback callback, AnimalStorage storage) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
        animalStorage = storage;
    }

    public void queueTask() {
        Log.e(TAG, "task added to the queue");
        mWorkerHandler.obtainMessage(DOWNLOAD_INFO).sendToTarget();
    }

    public void prepareHandler() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                final List<Animal> list = animalStorage.getAnimalList();
                mResponseHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onAnimalDownloaded(getRandomAnimal(list));
                    }
                });
                return true;
            }
        });
    }


    private Animal getRandomAnimal(List<Animal> list) {
        int number = (int) (Math.random() * 10);
        return list.get(number);
    }

}
