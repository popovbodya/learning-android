package ru.popov.bodya.eventsmanager;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.popov.bodya.eventsmanager.model.Event;

public class EventGenerator {

    private static final int LIST_SIZE = 20;
    private static final int STRING_SIZE = 40;
    private static final int START_CHAR = (int) 'A';
    private static final int END_CHAR = (int) 'Z';
    private static final Random RANDOM = new Random();


    public static Event createRandomEvent() {
        Event event = new Event();
        event.setTitle(createRandomString());
        event.setDescription(createRandomString());
        event.setDateStart(String.valueOf(createRandomLongNumber()));
        event.setDateEnd(String.valueOf(createRandomLongNumber()));
        return event;
    }

    public static List<Event> createRandomEventList() {
        List<Event> events = new ArrayList<>();
        int size = RANDOM.nextInt(LIST_SIZE) + 1;
        for (int i = 0; i < size; i++) {
            events.add(createRandomEvent());
        }
        return events;
    }

    private static String createRandomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < STRING_SIZE; i++) {
            int value = START_CHAR + RANDOM.nextInt(END_CHAR - START_CHAR);
            sb.append((char) value);
        }
        return sb.toString();
    }

    private static Long createRandomLongNumber() {
        return RANDOM.nextLong();
    }
}