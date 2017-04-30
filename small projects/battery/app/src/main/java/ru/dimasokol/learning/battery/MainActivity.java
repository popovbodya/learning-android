package ru.dimasokol.learning.battery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ImageView mBatteryIndicatorView;
    private TextView mBatteryPercentageView;
    private TextView mBatteryStatusView;
    private TextView mBatteryHealthView;
    private TextView mBatteryTemperatureView;
    private TextView mBatteryVoltageView;
    private TextView mBatteryTechnologyView;

    private BroadcastReceiver mBatteryStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Изменился заряд батарейки, отобразим его
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int maximumBattery = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int currentBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

                float fPercent = ((float) currentBattery / (float) maximumBattery) * 100f;

                int percent = Math.round(fPercent);
                int drawableLevel = 100 * percent;

                mBatteryIndicatorView.getDrawable().setLevel(drawableLevel);
                mBatteryPercentageView.setText(getString(R.string.battery_percentage, percent));

                int batteryStatus = getBatteryStatusFromIntent(intent);
                mBatteryStatusView.setText(getResources()
                        .getStringArray(R.array.battery_status)[batteryStatus]);

                int health = getBatteryHealthFromIntent(intent);

                mBatteryHealthView.setText(getResources()
                        .getStringArray(R.array.battery_health)[health]);

                int temperature = getBatteryTemperatureFromIntent(intent);
                int voltage = getBatteryVoltageFromIntent(intent);

                mBatteryTemperatureView.setText(getString(R.string.battery_temperature, (float) temperature / 10f));
                mBatteryVoltageView.setText(getString(R.string.battery_voltage, voltage));
                mBatteryTechnologyView.setText(getBatteryTechnologyFromIntent(intent));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBatteryIndicatorView = (ImageView) findViewById(R.id.battery_indicator);
        mBatteryPercentageView = (TextView) findViewById(R.id.battery_percentage);
        mBatteryStatusView = (TextView) findViewById(R.id.battery_status);
        mBatteryHealthView = (TextView) findViewById(R.id.battery_health);
        mBatteryTemperatureView = (TextView) findViewById(R.id.battery_temperature);
        mBatteryVoltageView = (TextView) findViewById(R.id.battery_voltage);
        mBatteryTechnologyView = (TextView) findViewById(R.id.battery_technology);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startListeningForBattery();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopListeningForBattery();
    }

    private void startListeningForBattery() {
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryStateReceiver, batteryFilter);
    }

    private void stopListeningForBattery() {
        unregisterReceiver(mBatteryStateReceiver);
    }

    private int getBatteryStatusFromIntent(Intent batteryIntent) {
        int status = 0;

        if (isBatteryPresent(batteryIntent)) {
            status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN);
        }

        return status;
    }

    private int getBatteryHealthFromIntent(Intent batteryIntent) {
        int health = 0;

        if (isBatteryPresent(batteryIntent)) {
            health = batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH,
                    BatteryManager.BATTERY_HEALTH_UNKNOWN);
        }

        return health;
    }

    private int getBatteryTemperatureFromIntent(Intent batteryIntent) {
        return batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
    }

    private int getBatteryVoltageFromIntent(Intent batteryIntent) {
        int voltage = batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        return (voltage < 100)? voltage * 1000 : voltage;
    }

    private String getBatteryTechnologyFromIntent(Intent batteryIntent) {
        String technology = getString(R.string.battery_technology_not_present);

        if (isBatteryPresent(batteryIntent)) {
            technology = batteryIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

            if (TextUtils.isEmpty(technology)) {
                technology = getString(R.string.battery_technology_unknown);
            }
        }

        return technology;
    }

    private boolean isBatteryPresent(Intent batteryIntent) {
        return batteryIntent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true);
    }
}
