package bodya.sbt.ru.currentwork;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.renderscript.RenderScript;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Worker extends HandlerThread {

    private static final String TAG = Worker.class.getSimpleName();
    private static final int DOWNLOAD_INFO = 0;

    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private Callback mCallback;

    public interface Callback {
        void onAnimalDownloaded(Animal animal);
    }

    public Worker(Handler responseHandler, Callback callback) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
    }

    public void queueTask() {
        Log.e(TAG, "task added to the queue");
        mWorkerHandler.obtainMessage(DOWNLOAD_INFO).sendToTarget();
    }

    public void prepareHandler() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                final List<Animal> list = ((MyApplication) ((MainActivity) mCallback).getApplication()).getAnimalList();
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
