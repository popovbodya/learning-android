package ru.popov.bodya.eventsmanager;


import android.app.Application;

import ru.popov.bodya.eventsmanager.db.DataBaseWorker;
import ru.popov.bodya.eventsmanager.db.ResolverEventDao;
import ru.popov.bodya.eventsmanager.interfaces.ModelProvider;
import ru.popov.bodya.eventsmanager.model.EditModeHolder;
import ru.popov.bodya.eventsmanager.model.EventStorage;

public class EventsManagerApplication extends Application implements ModelProvider {

    private EventStorage eventStorage;
    private DataBaseWorker dataBaseWorker;
    private EditModeHolder editModeHolder;


    @Override
    public void onCreate() {
        super.onCreate();
        ResolverEventDao animalsDao = new ResolverEventDao(this);
        eventStorage = new EventStorage(animalsDao);
        editModeHolder = new EditModeHolder(EditModeHolder.EditMode.Observe);
        dataBaseWorker = new DataBaseWorker(eventStorage);
        dataBaseWorker.start();
    }

    @Override
    public EventStorage getEventStorage() {
        return eventStorage;
    }

    @Override
    public EditModeHolder getEditModeHolder() {
        return editModeHolder;
    }


    @Override
    public DataBaseWorker getDataBaseWorker() {
        return dataBaseWorker;
    }

}
