package bodya.sbt.ru.currentwork.interfaces;


import bodya.sbt.ru.currentwork.async.DataBaseWorker;

public interface DataBaseLoaderProvider {
    DataBaseWorker getDataBaseWorker();
}
