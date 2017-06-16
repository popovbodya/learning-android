package ru.popov.bodya.eventsmanager.interfaces;


import ru.popov.bodya.eventsmanager.EditModeHolder;
import ru.popov.bodya.eventsmanager.EventStorage;
import ru.popov.bodya.eventsmanager.db.DataBaseWorker;

public interface ModelProvider {
    DataBaseWorker getDataBaseWorker();
    EventStorage getEventStorage();
    EditModeHolder getEditModeHolder();
}
