package com.tan_ds.networkservice;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

    private DownloadService.LocalBinder binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("MainActivity", "onServiceConnected");
            binder = (DownloadService.LocalBinder) service;
            binder.setForeground(false);
            displayState();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    private BroadcastReceiver changeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                displayState();
            }
        }
    };

    private View downloadButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = findViewById(R.id.button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 13);
                    return;
                }
                startDownload();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startDownload();
        }
    }

    private void startDownload() {
        Intent service = new Intent(MainActivity.this, DownloadService.class);
        service.setData(Uri.parse("https://farm4.staticflickr.com/3930/15544834082_3ebe7cf72d_o.jpg"));
        service.putExtra(DownloadService.EXTRA_FILE_NAME, "pic.jpg");
        startService(service);
        bindService(service, connection, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent service = new Intent(this, DownloadService.class);
        bindService(service, connection, 0);

        IntentFilter filter = new IntentFilter(DownloadService.ACTION_STATE_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(changeReceiver, filter);
        displayState();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (binder != null) {
            binder.setForeground(true);
            unbindService(connection);
        }

        LocalBroadcastManager.getInstance(this).unregisterReceiver(changeReceiver);
    }

    private void displayState() {
        if (binder == null) {
            downloadButton.setEnabled(true);
            progressBar.setProgress(0);

        } else {
            downloadButton.setEnabled(false);
            progressBar.setProgress(binder.getProgress());

            if (binder.isCompleted()) {
                unbindService(connection);
                binder = null;
                downloadButton.setEnabled(true);
            }
        }
    }
}
