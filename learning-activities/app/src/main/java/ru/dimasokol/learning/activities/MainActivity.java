package ru.dimasokol.learning.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView mWindowInstance;
    private TextView mActivityInstance;

    private View mRunStandard, mRunSingleTop, mRunLogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWindowInstance = (TextView) findViewById(R.id.window_instance);
        mActivityInstance = (TextView) findViewById(R.id.activity_instance);

        mActivityInstance.setText(getString(R.string.activity_format, System.identityHashCode(this)));
        mWindowInstance.setText(getString(R.string.window_format, System.identityHashCode(getWindow())));

        mRunStandard = findViewById(R.id.button_run_standard);
        mRunSingleTop = findViewById(R.id.button_run_single_top);
        mRunLogger = findViewById(R.id.button_run_logger);

        mRunStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runMeStandard();
            }
        });

        mRunSingleTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runMeSingleTop();
            }
        });

        mRunLogger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runLogger();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_new_intent).setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, getString(R.string.window_format,
                        System.identityHashCode(((AlertDialog) dialog).getWindow())), Toast.LENGTH_LONG)
                        .show();
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void runMeStandard() {
        Intent runMe = new Intent(this, MainActivity.class);
        startActivity(runMe);
    }

    private void runMeSingleTop() {
        Intent runMe = new Intent(this, MainActivity.class);
        runMe.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(runMe);
    }

    private void runLogger() {
        Intent logger = new Intent(this, LoggingActivity.class);
        startActivity(logger);
    }
}
