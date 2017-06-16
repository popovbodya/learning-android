package ru.popov.bodya.eventsmanager.db;


import android.content.ContentValues;
import android.database.Cursor;
import android.provider.CalendarContract;

import java.util.List;
import java.util.TimeZone;

import ru.popov.bodya.eventsmanager.model.Event;

public class EventInflateHelper {

    private static final byte DEFAULT_CALENDAR_ID = 1;

    public static void fillList(Cursor source, List<Event> target) {
        if (source.moveToFirst()) {
            while (!source.isAfterLast()) {
                target.add(createEventFromCursor(source));
                source.moveToNext();
            }
        }
    }

    public static ContentValues createValuesFromEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, event.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());
        values.put(CalendarContract.Events.DTSTART, event.getDateStart());
        values.put(CalendarContract.Events.DTEND, event.getDateEnd());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.CALENDAR_ID, DEFAULT_CALENDAR_ID);
        return values;
    }

    public static Event createEventFromCursor(Cursor cursor) {
        Event event = new Event();
        event.setId(getLong(cursor, CalendarContract.Events._ID));
        event.setDateStart(getString(cursor, CalendarContract.Events.DTSTART));
        event.setDateEnd(getString(cursor, CalendarContract.Events.DTEND));
        event.setDescription(getString(cursor, CalendarContract.Events.DESCRIPTION));
        event.setTitle(getString(cursor, CalendarContract.Events.TITLE));
        return event;
    }

    private static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    private static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

}
