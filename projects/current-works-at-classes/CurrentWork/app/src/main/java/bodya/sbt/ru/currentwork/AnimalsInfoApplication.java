package bodya.sbt.ru.currentwork;


import android.app.Application;

import bodya.sbt.ru.currentwork.db.AnimalsDao;
import bodya.sbt.ru.currentwork.db.SQLiteAnimalsDao;
import bodya.sbt.ru.currentwork.interfaces.AnimalsStorageProvider;

public class AnimalsInfoApplication extends Application implements AnimalsStorageProvider {

    private AnimalStorage animalsStorage;

    @Override
    public void onCreate() {
        super.onCreate();
        AnimalsDao animalsDao = new SQLiteAnimalsDao(this);
        animalsStorage = new AnimalStorage(animalsDao);
    }

    @Override
    public AnimalStorage getAnimalsStorage() {
        return animalsStorage;
    }
}
