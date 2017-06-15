package ru.popov.bodya.eventsmanager.db;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.popov.bodya.eventsmanager.Event;

public class ResolverEventDao implements EventDao {

    private static final String TAG = ResolverEventDao.class.getName();

    private Context appContext;

    public ResolverEventDao(Context context) {
        appContext = context;
    }

    @Override
    public long insertEvent(Event event) {
        return 0;
    }

    @Override
    public List<Event> getEvents() {
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

    private Cursor getEventsCursor() {
        ContentResolver resolver = appContext.getContentResolver();
        Cursor cursor = null;
        if (PermissionChecker.checkSelfPermission(appContext, Manifest.permission.READ_CALENDAR)
                + PermissionChecker.checkSelfPermission(appContext, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            cursor = resolver.query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
            Log.e(TAG, "" + CalendarContract.Events.CONTENT_URI);
        }
        return cursor;
    }
}
