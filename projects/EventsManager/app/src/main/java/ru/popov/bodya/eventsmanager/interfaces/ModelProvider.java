package ru.popov.bodya.eventsmanager.interfaces;


import ru.popov.bodya.eventsmanager.model.EditModeHolder;
import ru.popov.bodya.eventsmanager.model.EventStorage;
import ru.popov.bodya.eventsmanager.db.DataBaseWorker;

public interface ModelProvider {
    DataBaseWorker getDataBaseWorker();

    EventStorage getEventStorage();

    EditModeHolder getEditModeHolder();
}
