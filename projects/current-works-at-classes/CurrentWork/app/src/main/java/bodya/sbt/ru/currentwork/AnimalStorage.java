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

    AnimalStorage(AnimalsDao animalsDao) {
        this.animalsDao = animalsDao;
        onContentChangeListeners = new ArrayList<>();
    }

    public List<Animal> getAnimalList() {
        Log.e(TAG, "getAnimalList");
        setCachedAnimalList(animalsDao.getAnimals());
        return cachedAnimalList;
    }

    public void addAnimal(Animal animal) {
        animalsDao.insertAnimal(animal);
        notifyAllOnContentListeners();
    }

    public void deleteAnimal(Animal animal) {
        animalsDao.deleteAnimal(animal);
        notifyAllOnContentListeners();
    }

    public void updateAnimal(Animal animal) {
        if (!cachedAnimalList.contains(animal)) {
            animalsDao.updateAnimal(animal);
            notifyAllOnContentListeners();
        }
    }

    public List<Animal> getCachedAnimalList() {
        return cachedAnimalList;
    }

    Animal getAnimalByID(long animalId) {
        return animalsDao.getAnimalById(animalId);
    }

    public void addOnContentChangeListener(OnAnimalContentChangeListener listener) {
        onContentChangeListeners.add(listener);
    }

    public void removeOnContentChangeListener(OnAnimalContentChangeListener listener) {
        onContentChangeListeners.remove(listener);
    }

    void setCachedAnimalList(List<Animal> cachedAnimalList) {
        this.cachedAnimalList = cachedAnimalList;
    }

    private void notifyAllOnContentListeners() {
        for (OnAnimalContentChangeListener listener : onContentChangeListeners) {
            listener.onContentChanged();
        }
    }


}
