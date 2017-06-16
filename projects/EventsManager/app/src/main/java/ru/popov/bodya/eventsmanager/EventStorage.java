package ru.popov.bodya.eventsmanager;


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
        eventDao.insertEvent(event);
        notifyAllOnContentListeners();
    }

    public List<Event> getCachedEventList() {
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
