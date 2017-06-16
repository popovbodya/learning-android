package ru.popov.bodya.eventsmanager.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import ru.popov.bodya.eventsmanager.Event;
import ru.popov.bodya.eventsmanager.EventStorage;
import ru.popov.bodya.eventsmanager.fragments.DatePickerFragment;
import ru.popov.bodya.eventsmanager.R;
import ru.popov.bodya.eventsmanager.RecyclerViewAdapter;
import ru.popov.bodya.eventsmanager.fragments.TimePickerFragment;
import ru.popov.bodya.eventsmanager.db.DataBaseWorker;
import ru.popov.bodya.eventsmanager.interfaces.ModelProvider;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DataBaseWorker.LoaderCallback  {

    private static final String TAG = MainActivity.class.getName();
    private static final String DATE_PICKER_TAG = "datePicker";
    private static final String TIME_PICKER_TAG = "timePicker";
    private static final int PERMISSION_REQUEST_CODE = 0;

    private RecyclerViewAdapter recyclerViewAdapter;

    private DataBaseWorker dataBaseWorker;
    private EventStorage eventStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkReadWriteCalendarPermissions();

        ModelProvider modelProvider = (ModelProvider) getApplication();
        dataBaseWorker = modelProvider.getDataBaseWorker();
        dataBaseWorker.setListener(this);
        eventStorage = modelProvider.getEventStorage();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        if (savedInstanceState != null) {
            getCachedData();
        } else {
            dataBaseWorker.queueTask(new Runnable() {
                @Override
                public void run() {
                    onLoadFinished(eventStorage.getEventList());
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on handle click
                startActivity(ModifyEventActivity.newIntent(MainActivity.this));

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        dataBaseWorker.setListener(null);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.time_picker:
                showTimePickerDialog();
                break;
            case R.id.date_picker:
                showDatePickerDialog();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLoadFinished(List<Event> eventList) {
        Log.e(TAG, "onLoadFinished in MainActivity");
        recyclerViewAdapter.setEvents(eventList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // initLoader
                Log.e(TAG, "onRequestPermissionsResult");
            }
        }
    }

    private void checkReadWriteCalendarPermissions() {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                + PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            requestCalendarPermissions();
        } else {
            Log.e(TAG, "checkReadWriteCalendarPermissions");
            // initLoader
        }
    }

    private void requestCalendarPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
    }

    private void getCachedData() {
        Log.e(TAG, "getCachedData");
        recyclerViewAdapter.setEvents(eventStorage.getCachedEventList());
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), TIME_PICKER_TAG);
    }

}
