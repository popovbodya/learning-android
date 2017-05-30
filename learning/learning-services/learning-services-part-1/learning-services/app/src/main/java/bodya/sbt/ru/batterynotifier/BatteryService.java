package bodya.sbt.ru.batterynotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;

public class BatteryService extends Service {

    private static final int CRITICAL_BATTERY_LEVEL = 15;

    private boolean showNotifications = true;

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int maximumBattery = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int currentBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

                float fPercent = ((float) currentBattery / (float) maximumBattery) * 100f;


                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                int percent = Math.round(fPercent);

                if (percent < CRITICAL_BATTERY_LEVEL) {
                    if (showNotifications) {
                        Notification.Builder builder = new Notification.Builder(BatteryService.this);
                        builder.setSmallIcon(R.drawable.ic_stat_battery_low);
                        builder.setContentTitle(getString(R.string.low_battery_lvl));

                        Intent startBattery = new Intent();
                        startBattery.setComponent(new ComponentName("ru.dimasokol.learning.battery",
                                "ru.dimasokol.learning.battery.MainActivity"));
                        builder.setContentIntent(PendingIntent.getActivity(BatteryService.this, 0, startBattery,
                                PendingIntent.FLAG_CANCEL_CURRENT));

                        Intent startSelf = new Intent(BatteryService.this, BatteryService.class);
                        startSelf.setAction(Intent.ACTION_DELETE);

                        builder.setDeleteIntent(PendingIntent.getService(BatteryService.this, 1, startSelf,
                                PendingIntent.FLAG_CANCEL_CURRENT));

                        Notification notification = builder.getNotification();
                        notificationManager.notify(R.string.low_battery_lvl, notification);
                    }
                } else {
                    notificationManager.cancel(R.string.low_battery_lvl);
                    showNotifications = true;
                }

            }
        }
    };

    public BatteryService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, batteryFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && Intent.ACTION_DELETE.equals(intent.getAction())) {
            showNotifications = false;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
