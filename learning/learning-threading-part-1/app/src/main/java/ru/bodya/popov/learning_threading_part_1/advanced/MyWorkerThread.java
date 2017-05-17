package ru.bodya.popov.learning_threading_part_1.advanced;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyWorkerThread extends HandlerThread {

    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private static final String TAG = MyWorkerThread.class.getSimpleName();
    private Map<ImageView, String> mRequestMap = new HashMap<>();
    private Callback mCallback;

    public interface Callback {
        public void onImageDownloaded(ImageView imageView, Bitmap bitmap, int side);
    }

    public MyWorkerThread(Handler responseHandler, Callback callback) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
    }

    public void queueTask(String url, int side, ImageView imageView) {
        mRequestMap.put(imageView, url);
        Log.i(TAG, url + " added to the queue");
        mWorkerHandler.obtainMessage(side, imageView).sendToTarget();
    }

    public void prepareHandler() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ImageView imageView = (ImageView) msg.obj;
                String side = msg.what == MyActivity.LEFT_SIDE ? "left side" : "right side";
                Log.i(TAG, String.format("Processing %s, %s", mRequestMap.get(imageView), side));

                handleRequest(imageView, msg.what);

                return true;
            }
        });
    }

    private void handleRequest(final ImageView imageView, final int side) {

        try {

            InputStream in = new java.net.URL(mRequestMap.get(imageView)).openStream();
            final Bitmap myBitmap = BitmapFactory.decodeStream(in);
            mRequestMap.remove(imageView);
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onImageDownloaded(imageView, myBitmap, side);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}