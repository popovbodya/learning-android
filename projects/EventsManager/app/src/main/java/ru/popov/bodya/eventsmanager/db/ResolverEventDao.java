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

import ru.popov.bodya.eventsmanager.model.Event;


public class ResolverEventDao implements EventDao {

    private static final String TAG = ResolverEventDao.class.getName();
    private static final long NO_ID = -1;
    private static final short ROW_UPDATE_STATUS = 0;
    private static final short ROW_COUNT_UPDATED = 0;

    private Context appContext;

    public ResolverEventDao(Context context) {
        appContext = context;
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

    @SuppressWarnings("MissingPermission")
    @Override
    public Event getEventById(long id) {
        Cursor cursor;
        Event event = null;
        ContentResolver contentResolver = appContext.getContentResolver();
        if (permissionsGranted()) {
            cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, null, CalendarContract.Events._ID + "=?", new String[]{String.valueOf(id)}, null);
            if (cursor != null && cursor.moveToFirst()) {
                event = EventInflateHelper.createEventFromCursor(cursor);
            }
        }

        return event;
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

    @SuppressWarnings("MissingPermission")
    @Override
    public int updateEvent(Event event) {
        int rowUpdated = ROW_COUNT_UPDATED;
        ContentResolver resolver = appContext.getContentResolver();
        ContentValues contentValues = EventInflateHelper.createValuesFromEvent(event);
        if (permissionsGranted()) {
            rowUpdated = resolver.update(CalendarContract.Events.CONTENT_URI, contentValues, CalendarContract.Events._ID + "=?", new String[]{String.valueOf(event.getId())});
        }
        return rowUpdated;
    }


    @SuppressWarnings("MissingPermission")
    @Override
    public int deleteEvent(Event event) {
        int animalDeletedStatus = ROW_UPDATE_STATUS;
        ContentResolver resolver = appContext.getContentResolver();
        if (permissionsGranted()) {
            animalDeletedStatus = resolver.delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events._ID + "=?", new String[]{String.valueOf(event.getId())});
        }
        return animalDeletedStatus;
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
