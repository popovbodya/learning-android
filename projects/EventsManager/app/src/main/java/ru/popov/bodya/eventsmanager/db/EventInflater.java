package ru.popov.bodya.eventsmanager.db;


import android.database.Cursor;
import android.provider.CalendarContract;

import java.util.List;

import ru.popov.bodya.eventsmanager.Event;

public class EventInflater {

    public static void fillList(Cursor source, List<Event> target) {
        if (source.moveToFirst()) {
            while (!source.isAfterLast()) {
                target.add(createCallFromCursor(source));
                source.moveToNext();
            }
        }
    }

    private static Event createCallFromCursor(Cursor cursor) {
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
