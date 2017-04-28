package ru.dimasokol.learning.activities;

import android.app.Application;

/**
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */

public class ActivitySampleApplication extends Application {

    private LoggingStorage mStorage = new LoggingStorage();

    public LoggingStorage getStorage() {
        return mStorage;
    }

}
