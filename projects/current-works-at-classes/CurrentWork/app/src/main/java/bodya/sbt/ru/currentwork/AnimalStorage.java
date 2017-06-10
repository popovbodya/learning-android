package bodya.sbt.ru.currentwork;


import java.util.ArrayList;
import java.util.List;

import bodya.sbt.ru.currentwork.db.AnimalsDao;
import bodya.sbt.ru.currentwork.interfaces.OnAnimalContentChangeListener;

public class AnimalStorage {

    private final AnimalsDao animalsDao;
    private final List<OnAnimalContentChangeListener> onContentChangeListeners;

    public AnimalStorage(AnimalsDao animalsDao) {
        this.animalsDao = animalsDao;
        onContentChangeListeners = new ArrayList<>();
    }

    public List<Animal> getAnimalList() {
        return animalsDao.getAnimals();
    }

    public void addAnimal(Animal animal) {
        animalsDao.insertAnimal(animal);
        notifyAllOnContentListeners(animal);

    }

    public void deleteAnimal(Animal animal) {
        animalsDao.deleteAnimal(animal);
        notifyAllOnContentListeners(animal);
    }

    public void updateAnimal(Animal animal) {
        animalsDao.updateAnimal(animal);
        notifyAllOnContentListeners(animal);
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

    public void notifyAllOnContentListeners(Animal animal) {
        for (OnAnimalContentChangeListener listener : onContentChangeListeners) {
            listener.onContentChanged(animal);
        }
    }


}
