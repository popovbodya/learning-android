package ru.boyda.popov.learning_threading_part_1.basics;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import ru.boyda.popov.learning_threading_part_1.R;


public class ModifiedActivity extends Activity {

    private Handler mUiHandler = new Handler();
    private MyWorkerThread mWorkerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWorkerThread = new MyWorkerThread("myWorkerThread");

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == 0) {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifiedActivity.this,
                                        "I am at the start of background task", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                    if (i == 2) {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifiedActivity.this,
                                        "I am at the middle of background task", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                }
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ModifiedActivity.this, "Background task is completed", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        };

        mWorkerThread.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mWorkerThread.postTask(task);
        mWorkerThread.postTask(task);
    }

    @Override
    protected void onDestroy() {
        mWorkerThread.quit();
        super.onDestroy();
    }
}
