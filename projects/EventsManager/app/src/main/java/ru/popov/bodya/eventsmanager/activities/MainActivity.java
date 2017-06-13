package ru.popov.bodya.eventsmanager.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import ru.popov.bodya.eventsmanager.DataBaseLoaderFunctions;
import ru.popov.bodya.eventsmanager.DatePickerFragment;
import ru.popov.bodya.eventsmanager.Event;
import ru.popov.bodya.eventsmanager.EventsLoader;
import ru.popov.bodya.eventsmanager.EventsManagerApplication;
import ru.popov.bodya.eventsmanager.R;
import ru.popov.bodya.eventsmanager.RecyclerViewAdapter;
import ru.popov.bodya.eventsmanager.TimePickerFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();
    private static final String DATE_PICKER_TAG = "datePicker";
    private static final String TIME_PICKER_TAG = "timePicker";
    private static final int LOADER_ID = 1;
    private static final int PERMISSION_REQUEST_CODE = 0;

    private RecyclerViewAdapter recyclerViewAdapter;
    private EventsManagerApplication eventsManagerApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventsManagerApplication = (EventsManagerApplication) getApplication();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on handle click
                eventsManagerApplication.addTaskToQueue(DataBaseLoaderFunctions.CREATE_EVENT);
                eventsManagerApplication.getEventsLoader().forceLoad();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkReadWriteCalendarPermissions();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // initLoader
                getSupportLoaderManager().initLoader(LOADER_ID, null, new EventsLoaderCallbacks());
                Log.e(TAG, "onRequestPermissionsResult");
            }
        }
    }

    void onDatePicked(int hourOfDay, int minute) {
        Log.e(TAG, "onDatePicked with time: hours = " + hourOfDay + ", minute = " + minute);
        showTimePickerDialog();
    }

    private void checkReadWriteCalendarPermissions() {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                + PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            requestBothPermissions();
        } else {
            Log.e(TAG, "checkReadWriteCalendarPermissions");
            getSupportLoaderManager().initLoader(LOADER_ID, null, new EventsLoaderCallbacks());
        }
    }

    private void requestBothPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), TIME_PICKER_TAG);
    }

    private class EventsLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Event>> {

        @Override
        public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
            EventsLoader eventsLoader = new EventsLoader(MainActivity.this);
            eventsManagerApplication.setEventsLoader(eventsLoader);
            return eventsLoader;
        }

        @Override
        public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
            Log.e(TAG, "onLoadFinished");
            recyclerViewAdapter.setEvents(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Event>> loader) {

        }
    }

}
