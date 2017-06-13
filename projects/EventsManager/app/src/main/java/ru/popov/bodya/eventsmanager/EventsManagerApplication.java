package ru.popov.bodya.eventsmanager;


import android.app.Application;

import java.util.ArrayDeque;
import java.util.Queue;

public class EventsManagerApplication extends Application {


    private Queue<Byte> dbTasksQueue;
    private EventsLoader eventsLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        dbTasksQueue = new ArrayDeque<>();
    }

    public EventsLoader getEventsLoader() {
        return eventsLoader;
    }

    public void setEventsLoader(EventsLoader eventsLoader) {
        this.eventsLoader = eventsLoader;
    }

    public synchronized void addTaskToQueue(byte task) {
        dbTasksQueue.add(task);
    }

    public synchronized Byte getTaskFromQueue() {
        return dbTasksQueue.poll();
    }
}
