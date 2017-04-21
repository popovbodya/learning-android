package ru.dimasokol.learning.packages;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InfoActivity extends Activity {

    private View mLaunchButton;
    private ListView mInfoListView;
    private PackageInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        String packageName = getIntent().getData().getHost();

        mLaunchButton = findViewById(R.id.button_launch);
        mInfoListView = (ListView) findViewById(R.id.package_info);

        mLaunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = getIntent().getData().getHost();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);

                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
            }
        });

        mLaunchButton.setEnabled(canBeLaunched(packageName));

        displayPackage(packageName);
    }

    private void displayPackage(String packageName) {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES | PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            finish();
            return;
        }
        getActionBar().setTitle(info.applicationInfo != null?
                getPackageManager().getApplicationLabel(info.applicationInfo)
                :
                packageName
        );

        List<ActivityInfo> activities = (info.activities != null)?
                Arrays.asList(info.activities)
                :
                Collections.<ActivityInfo>emptyList();

        List<String> permissions = (info.requestedPermissions != null)?
                Arrays.asList(info.requestedPermissions)
                :
                Collections.<String>emptyList();

        mAdapter = new PackageInfoAdapter(activities, permissions);
        mInfoListView.setAdapter(mAdapter);
    }

    private boolean canBeLaunched(String packageName) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        return launchIntent != null;
    }
}
