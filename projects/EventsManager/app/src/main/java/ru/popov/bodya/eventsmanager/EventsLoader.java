package ru.popov.bodya.eventsmanager;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EventsLoader extends AsyncTaskLoader<List<Event>> {

    private static final String TAG = EventsLoader.class.getName();


    public EventsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Event> loadInBackground() {
        Byte command = ((EventsManagerApplication) getContext().getApplicationContext()).getTaskFromQueue();
        if (command != null && command == DataBaseLoaderFunctions.CREATE_EVENT) {
            Log.e(TAG, "loadInBackground" + " CREATE EVENT");
        }
        List<Event> eventList = new ArrayList<>();

        Cursor cursor = getEventsCursor();

        if (cursor != null) {
            EventInflater.fillList(cursor, eventList);
            cursor.close();
        } else {
            Log.e(TAG, "cursor is null");
        }


        return eventList;
    }

    private Cursor getEventsCursor() {
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = null;
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR)
                + PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            cursor = resolver.query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
            Log.e(TAG, "" + CalendarContract.Events.CONTENT_URI);
        }
        return cursor;
    }


}
