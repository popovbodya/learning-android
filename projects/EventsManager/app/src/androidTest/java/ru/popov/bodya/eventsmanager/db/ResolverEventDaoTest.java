package ru.popov.bodya.eventsmanager.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.popov.bodya.eventsmanager.EventGenerator;
import ru.popov.bodya.eventsmanager.model.Event;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;

@RunWith(AndroidJUnit4.class)
public class ResolverEventDaoTest {

    private ResolverEventDao resolverEventDao;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        resolverEventDao = new ResolverEventDao(context);
    }

    @Test
    public void testInsert() {
        Event event = EventGenerator.createRandomEvent();
        long id = resolverEventDao.insertEvent(event);
        assertThat(true, is(id > 0));
    }

    @Test
    public void testGetEventById() {
        Event expected = EventGenerator.createRandomEvent();
        long id = resolverEventDao.insertEvent(expected);
        expected.setId(id);
        Event actualEvent = resolverEventDao.getEventById(id);
        assertThat(actualEvent, is(expected));
    }

    @Test
    public void testDeleteEvent() {
        int expected = 1;
        Event event = EventGenerator.createRandomEvent();
        long id = resolverEventDao.insertEvent(event);
        event.setId(id);
        int actual = resolverEventDao.deleteEvent(event);
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetEvents() {
        List<Event> expected = new ArrayList<>();

        expected.addAll(EventGenerator.createRandomEventList());
        expected.addAll(resolverEventDao.getEvents());

        for (Event event : expected) {
            if (event.getId() == 0) {
                event.setId(resolverEventDao.insertEvent(event));
            }
        }
        List<Event> actual = resolverEventDao.getEvents();
        assertThat(actual, everyItem(isIn(expected)));
    }

    @Test
    public void testUpdateEvent() {
        Event expected = EventGenerator.createRandomEvent();
        long id = resolverEventDao.insertEvent(expected);
        expected.setId(id);
        expected.setDateStart(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        expected.setDateEnd(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        resolverEventDao.updateEvent(expected);

        Event actual = resolverEventDao.getEventById(id);
        assertThat(actual, is(expected));

    }

    @After
    public void tearDown() {
        for (Event event : resolverEventDao.getEvents()) {
            resolverEventDao.deleteEvent(event);
        }
    }
}
