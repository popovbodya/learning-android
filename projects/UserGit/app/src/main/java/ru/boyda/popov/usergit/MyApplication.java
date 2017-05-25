package ru.boyda.popov.usergit;


import android.app.Application;

import ru.boyda.popov.usergit.interfaces.UsersStorageProvider;
import ru.boyda.popov.usergit.storages.UsersStorage;

public class MyApplication extends Application implements UsersStorageProvider {

    private UsersStorage usersStorage = new UsersStorage();

    @Override
    public UsersStorage getUsersStorage() {
        return usersStorage;
    }

}