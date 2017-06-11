package bodya.sbt.ru.currentwork;


import android.app.Application;

import bodya.sbt.ru.currentwork.async.DataBaseWorker;
import bodya.sbt.ru.currentwork.db.AnimalsDao;
import bodya.sbt.ru.currentwork.db.SQLiteAnimalsDao;
import bodya.sbt.ru.currentwork.interfaces.DataBaseLoaderProvider;

public class AnimalsInfoApplication extends Application implements DataBaseLoaderProvider {

    private AnimalStorage animalsStorage;
    private DataBaseWorker dataBaseWorker;

    @Override
    public void onCreate() {
        super.onCreate();
        AnimalsDao animalsDao = new SQLiteAnimalsDao(this);
        animalsStorage = new AnimalStorage(animalsDao);

        dataBaseWorker = new DataBaseWorker(animalsStorage);
        dataBaseWorker.start();
    }

    public AnimalStorage getAnimalsStorage() {
        return animalsStorage;
    }

    @Override
    public DataBaseWorker getDataBaseWorker() {
        return dataBaseWorker;
    }
}
