package ru.popov.bodya.gitwatcher;


import android.app.Application;

public class GitWatcherApplication extends Application {

    private UserStorage userStorage;

    @Override
    public void onCreate() {
        super.onCreate();
        userStorage = new UserStorage();
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
