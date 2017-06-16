package ru.popov.bodya.eventsmanager.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import ru.popov.bodya.eventsmanager.model.EditModeHolder;
import ru.popov.bodya.eventsmanager.model.Event;
import ru.popov.bodya.eventsmanager.model.EventStorage;
import ru.popov.bodya.eventsmanager.R;
import ru.popov.bodya.eventsmanager.RecyclerViewAdapter;
import ru.popov.bodya.eventsmanager.db.DataBaseWorker;
import ru.popov.bodya.eventsmanager.interfaces.ModelProvider;

public class EventInfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DataBaseWorker.LoaderCallback {

    private static final String TAG = EventInfoActivity.class.getName();
    private static final int PERMISSION_REQUEST_CODE = 0;

    private RecyclerViewAdapter recyclerViewAdapter;
    private TextView editModeTextView;

    private DataBaseWorker dataBaseWorker;
    private EventStorage eventStorage;
    private EditModeHolder editModeHolder;


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
        editModeHolder = modelProvider.getEditModeHolder();
        editModeTextView = (TextView) findViewById(R.id.text_view_mode);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
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

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ModifyEventActivity.newIntent(EventInfoActivity.this));
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
    protected void onStart() {
        super.onStart();
        EditModeHolder.EditMode editMode = editModeHolder.getEditMode();
        switch (editMode) {
            case Observe:
                editModeTextView.setText(getResources().getString(R.string.observe_mode));
                break;
            case Delete:
                editModeTextView.setText(getResources().getString(R.string.delete_mode));
                break;
            case Update:
                editModeTextView.setText(getResources().getString(R.string.update_mode));
        }
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.observe_mode:
                editModeHolder.setEditMode(EditModeHolder.EditMode.Observe);
                editModeTextView.setText(getResources().getString(R.string.observe_mode));
                break;
            case R.id.update_mode:
                editModeHolder.setEditMode(EditModeHolder.EditMode.Update);
                editModeTextView.setText(getResources().getString(R.string.update_mode));
                break;
            case R.id.delete_mode:
                editModeHolder.setEditMode(EditModeHolder.EditMode.Delete);
                editModeTextView.setText(getResources().getString(R.string.delete_mode));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLoadFinished(List<Event> eventList) {
        Log.e(TAG, "onLoadFinished in EventInfoActivity");
        recyclerViewAdapter.setEvents(eventList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.wtf(TAG, "Permissions granted, congratz");
            }
        }
    }

    private void checkReadWriteCalendarPermissions() {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                + PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            requestCalendarPermissions();
        }
    }

    private void requestCalendarPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
    }

    private void getCachedData() {
        Log.e(TAG, "getCachedData");
        recyclerViewAdapter.setEvents(eventStorage.getCachedEventList());
    }

}
