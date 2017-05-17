package ru.boyda.popov.learning_threading_part_1.basics;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import ru.boyda.popov.learning_threading_part_1.R;

public class BasicsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private class LooperThread extends Thread {

        private Handler mHandler;

        public void run() {
            Looper.prepare();


            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // process incoming messages here
                    // this will run in non-ui/background thread
                }
            };

            Looper.loop();
        }
    }

    private class MyHandlerThread extends HandlerThread {

        Handler handler;

        public MyHandlerThread(String name) {
            super(name);
        }

        @Override
        protected void onLooperPrepared() {
            handler = new Handler(getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    // process incoming messages here
                    // this will run in non-ui/background thread
                }
            };
        }
    }
}
