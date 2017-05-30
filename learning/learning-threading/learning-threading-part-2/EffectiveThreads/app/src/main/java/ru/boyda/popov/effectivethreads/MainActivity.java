package ru.boyda.popov.effectivethreads;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final static int HIDE_PROGRESS_BAR = 0;
    private final static int SHOW_PROGRESS_BAR = 1;

    private Worker worker;

    private TextView textView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler mainHandler = new Handler(new HandlerCallBack());
        worker = new Worker(mainHandler);
        worker.start();

        textView = (TextView) findViewById(R.id.text_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worker.doWork();
            }
        });

        showProgressBar(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        worker.exit();
    }

    private class HandlerCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PROGRESS_BAR:
                    showProgressBar(true);
                    break;
                case HIDE_PROGRESS_BAR:
                    showProgressBar(false);
                    textView.setText(String.valueOf(msg.arg1));
            }
            return true;
        }
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
