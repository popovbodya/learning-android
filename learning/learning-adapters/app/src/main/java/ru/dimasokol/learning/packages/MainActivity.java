package ru.dimasokol.learning.packages;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {

    private ListView mPackagesListView;
    private PackagesAdapter mPackagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPackagesListView = (ListView) findViewById(R.id.packages_list);
        mPackagesAdapter = new PackagesAdapter(getPackageManager().getInstalledPackages(0));
        mPackagesListView.setAdapter(mPackagesAdapter);

        mPackagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageInfo packageInfo = mPackagesAdapter.getItem(position);
                Uri data = Uri.parse("package://" + packageInfo.packageName);
                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                viewIntent.setData(data);
                startActivity(viewIntent);
            }
        });
    }
}
