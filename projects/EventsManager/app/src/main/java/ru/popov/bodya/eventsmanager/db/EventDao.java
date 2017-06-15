package ru.popov.bodya.eventsmanager.db;


import java.util.List;

import ru.popov.bodya.eventsmanager.Event;

public interface EventDao {
    long insertEvent(Event event);

    List<Event> getEvents();

    Event getEventById(long id);

    int updateEvent(Event event);

    int deleteEvent(Event event);
}
