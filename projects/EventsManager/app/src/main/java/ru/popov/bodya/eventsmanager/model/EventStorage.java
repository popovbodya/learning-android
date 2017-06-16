package ru.popov.bodya.eventsmanager.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.popov.bodya.eventsmanager.db.EventDao;
import ru.popov.bodya.eventsmanager.interfaces.OnEventContentChangeListener;

public class EventStorage {

    private static final String TAG = EventStorage.class.getName();

    private final EventDao eventDao;
    private final List<OnEventContentChangeListener> onEventContentChangeListeners;
    private List<Event> cachedEventList;

    public EventStorage(EventDao eventDao) {
        this.eventDao = eventDao;
        onEventContentChangeListeners = new ArrayList<>();
    }

    public List<Event> getEventList() {
        Log.e(TAG, "getEventList");
        cachedEventList = eventDao.getEvents();
        return cachedEventList;
    }

    public void addEvent(Event event) {
        Log.e(TAG, "addEvent");
        eventDao.insertEvent(event);
        notifyAllOnContentListeners();
    }

    public void deleteEvent(Event event) {
        Log.e(TAG, "deleteEvent");
        eventDao.deleteEvent(event);
        notifyAllOnContentListeners();
    }

    public void updateEvent(Event event) {
        Log.e(TAG, "updateEvent");
        if (!cachedEventList.contains(event)) {
            eventDao.updateEvent(event);
            notifyAllOnContentListeners();
        }
    }

    public List<Event> getCachedEventList() {
        Log.e(TAG, "getCachedEventList");
        return cachedEventList;
    }

    public void addOnContentChangeListener(OnEventContentChangeListener listener) {
        onEventContentChangeListeners.add(listener);
    }

    public void removeOnContentChangeListener(OnEventContentChangeListener listener) {
        onEventContentChangeListeners.remove(listener);
    }

    private void notifyAllOnContentListeners() {
        for (OnEventContentChangeListener listener : onEventContentChangeListeners) {
            listener.onContentChanged();
        }
    }
}
