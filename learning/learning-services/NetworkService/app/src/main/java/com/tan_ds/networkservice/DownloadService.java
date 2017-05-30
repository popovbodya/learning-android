package com.tan_ds.networkservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DownloadService extends IntentService {


    public static final String EXTRA_FILE_NAME = "file_name";
    public static final String ACTION_STATE_CHANGED = "download_state_changed";
    private boolean completed = false;
    private boolean withErrors = false;
    private int progress = 0;
    private static final int NOTIFICATION_ID = 100500;
    private boolean foregroundMode = false;

    public DownloadService() {
        super("DownloadService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("DownloadService", "onHandleIntent");
            URL url;
        try {
            url = new URL(intent.getDataString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        String filename = intent.getStringExtra(EXTRA_FILE_NAME);

        try {
            downloadFile(url, filename);
            withErrors = false;
            progress = 100;

        } catch (IOException e) {
           withErrors = true;
           progress = 0;
        } finally {
            completed = true;
            notifyStateChanged();
        }
    }


    private void downloadFile(URL url, String filename) throws IOException{

        Log.e("downloading", ""+Thread.currentThread().getId());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int size = connection.getContentLength();

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        directory.mkdirs();

        File file = new File(directory, filename);

        BufferedInputStream is =
                new BufferedInputStream(connection.getInputStream());
        BufferedOutputStream os =
                new BufferedOutputStream(new FileOutputStream(file));

        byte[] buffer = new byte[8096];
        int reader;
        int downloader = 0;

        while ((reader = is.read(buffer)) > 0){
            os.write(buffer, 0, reader);
            downloader += reader;

            int percent = calculatePercents(size, downloader);
            if (percent != progress){
                progress = percent;

                if (foregroundMode){
                    Notification notification = buildNotification();
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(NOTIFICATION_ID, notification);
                }
                notifyStateChanged();
            }

        }

        os.flush();
        os.close();

        connection.disconnect();
    }

    private void notifyStateChanged(){
        Intent data  = new Intent(ACTION_STATE_CHANGED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(data);
    }

    private int calculatePercents(int total, int current){
        int onPercent = Math.max(1, total/100);
        return  Math.min(current/onPercent, 100);
    }

    private Notification buildNotification(){
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_download, progress*100);
        builder.setProgress(100, progress, false);
        builder.setContentTitle(getString(R.string.notification_file_downloading));
        return builder.build();
    }

    public class LocalBinder extends Binder{
        public boolean isCompleted(){
            return completed;
        }
        public boolean hasErrors(){
            return withErrors;
        }
        public int getProgress(){
            return progress;
        }

        public void setForeground(boolean foreground){
            if (foreground){
                startForeground(NOTIFICATION_ID, buildNotification());
            }else{
                stopForeground(true);
            }

            foregroundMode = foreground;
        }

    }

}