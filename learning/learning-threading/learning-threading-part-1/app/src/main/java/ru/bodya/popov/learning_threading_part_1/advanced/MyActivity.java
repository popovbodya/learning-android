package ru.bodya.popov.learning_threading_part_1.advanced;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

import ru.bodya.popov.learning_threading_part_1.R;


public class MyActivity extends Activity implements MyWorkerThread.Callback {

    private static boolean isVisible;
    public static final int LEFT_SIDE = 0;
    public static final int RIGHT_SIDE = 1;
    private LinearLayout mLeftSideLayout;
    private LinearLayout mRightSideLayout;
    private MyWorkerThread mWorkerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_main);
        isVisible = true;
        mLeftSideLayout = (LinearLayout) findViewById(R.id.leftSideLayout);
        mRightSideLayout = (LinearLayout) findViewById(R.id.rightSideLayout);

        String imageURL = "http://droidtune.com/wp-content/uploads/2013/11/%D0%9B%D1%83%D1%87%D1%88%D0%B8%D0%B5-Android-%D0%B8%D0%B3%D1%80%D1%8B-%D1%80%D0%B0%D0%B7%D0%BD%D1%96%D1%85-%D0%B6%D0%B0%D0%BD%D1%80%D0%BE%D0%B2.png";
        String[] urls = new String[]{imageURL, imageURL, imageURL, imageURL, imageURL};

        mWorkerThread = new MyWorkerThread(new Handler(), this);
        mWorkerThread.start();
        mWorkerThread.prepareHandler();
        Random random = new Random();
        for (String url : urls){
            mWorkerThread.queueTask(url, random.nextInt(2), new ImageView(this));
        }
    }

    @Override
    protected void onPause() {
        isVisible = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWorkerThread.quit();
        super.onDestroy();
    }

    @Override
    public void onImageDownloaded(ImageView imageView, Bitmap bitmap, int side) {
        imageView.setImageBitmap(bitmap);

        if (isVisible && side == LEFT_SIDE){
            mLeftSideLayout.addView(imageView);
        } else if (isVisible && side == RIGHT_SIDE){
            mRightSideLayout.addView(imageView);
        }
    }
}