package bodya.sbt.ru.currentwork;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bodya.sbt.ru.currentwork.db.AnimalsDao;
import bodya.sbt.ru.currentwork.interfaces.OnAnimalContentChangeListener;

public class AnimalStorage {

    private static final String TAG = "AnimalStorage";

    private final AnimalsDao animalsDao;
    private final List<OnAnimalContentChangeListener> onContentChangeListeners;
    private List<Animal> cachedAnimalList;

    public AnimalStorage(AnimalsDao animalsDao) {
        this.animalsDao = animalsDao;
        onContentChangeListeners = new ArrayList<>();
    }

    public List<Animal> getAnimalList() {
        Log.e(TAG, "getAnimalList");
        cachedAnimalList = animalsDao.getAnimals();
        notifyAllOnContentListeners(cachedAnimalList);
        return cachedAnimalList;
    }

    public void addAnimal(Animal animal) {
        animalsDao.insertAnimal(animal);
        getAnimalList();
    }

    public void deleteAnimal(Animal animal) {
        animalsDao.deleteAnimal(animal);
        getAnimalList();
    }

    public void updateAnimal(Animal animal) {
        animalsDao.updateAnimal(animal);
        getAnimalList();
    }

    public List<Animal> getCachedAnimalList() {
        return cachedAnimalList;
    }

    public Animal getAnimalByID(long animalId) {
        return animalsDao.getAnimalById(animalId);
    }

    public void addOnContentChangeListener(OnAnimalContentChangeListener listener) {
        onContentChangeListeners.add(listener);
    }

    public void removeOnContentChangeListener(OnAnimalContentChangeListener listener) {
        onContentChangeListeners.remove(listener);
    }

    private void notifyAllOnContentListeners(List<Animal> loadedList) {
        for (OnAnimalContentChangeListener listener : onContentChangeListeners) {
            listener.onContentChanged(loadedList);
        }
    }


}
