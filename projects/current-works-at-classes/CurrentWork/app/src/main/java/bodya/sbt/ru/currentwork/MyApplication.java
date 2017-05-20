package bodya.sbt.ru.currentwork;


import android.app.Application;

import bodya.sbt.ru.currentwork.interfaces.AnimalsStorageProvider;

public class MyApplication extends Application implements AnimalsStorageProvider {

    private AnimalStorage animalsStorage;

    @Override
    public void onCreate() {
        super.onCreate();
        animalsStorage = new AnimalStorage();
    }

    @Override
    public AnimalStorage getAnimalsStorage() {
        return animalsStorage;
    }

}
