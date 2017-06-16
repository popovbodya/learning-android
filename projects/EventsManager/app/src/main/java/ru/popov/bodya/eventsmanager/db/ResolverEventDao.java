package ru.popov.bodya.eventsmanager.db;


import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.popov.bodya.eventsmanager.Event;

public class ResolverEventDao implements EventDao {

    private static final String TAG = ResolverEventDao.class.getName();
    private static final long NO_ID = -1;

    private Context appContext;

    public ResolverEventDao(Context context) {
        appContext = context;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public long insertEvent(Event event) {
        long id = NO_ID;
        ContentValues contentValues = EventInflateHelper.createValuesFromEvent(event);
        ContentResolver contentResolver = appContext.getContentResolver();
        if (permissionsGranted()) {
            Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);
            id = ContentUris.parseId(uri);
        }
        return id;
    }

    @Override
    public List<Event> getEvents() {
        List<Event> eventList = new ArrayList<>();

        Cursor cursor = getEventsCursor();

        if (cursor != null) {
            EventInflateHelper.fillList(cursor, eventList);
            cursor.close();
        } else {
            Log.e(TAG, "cursor is null");
        }
        return eventList;
    }

    @Override
    public Event getEventById(long id) {
        return null;
    }

    @Override
    public int updateEvent(Event event) {
        return 0;
    }

    @Override
    public int deleteEvent(Event event) {
        return 0;
    }

    @SuppressWarnings("MissingPermission")
    private Cursor getEventsCursor() {
        ContentResolver resolver = appContext.getContentResolver();
        Cursor cursor = null;
        if (permissionsGranted()) {
            cursor = resolver.query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
            Log.e(TAG, "" + CalendarContract.Events.CONTENT_URI);
        }
        return cursor;
    }


    private boolean permissionsGranted() {
        return PermissionChecker.checkSelfPermission(appContext, Manifest.permission.READ_CALENDAR)
                + PermissionChecker.checkSelfPermission(appContext, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }


}
