package bodya.sbt.ru.currentwork.interfaces;


import bodya.sbt.ru.currentwork.AnimalStorage;
import bodya.sbt.ru.currentwork.async.DataBaseWorker;

public interface ModelProvider {
    DataBaseWorker getDataBaseWorker();
    AnimalStorage getAnimalsStorage();

}
