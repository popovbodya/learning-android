package bodya.sbt.ru.currentwork;


import java.util.ArrayList;
import java.util.List;

import bodya.sbt.ru.currentwork.interfaces.OnAnimalContentChangeListener;

public class AnimalStorage {

    private List<Animal> animalList;
    private final List<OnAnimalContentChangeListener> onContentChangeListeners;

    public AnimalStorage() {
        onContentChangeListeners = new ArrayList<>();
        createAnimalList();
    }

    private void createAnimalList() {
        animalList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int age = (int) (Math.random() * 15) + 1;
            int height = (int) (Math.random() * 50) + 1;
            int weight = (int) (Math.random() * 50) + 1;
            animalList.add(new Animal("Animal " + i, age, height, weight));
        }
    }

    public List<Animal> getAnimalList() {
        return new ArrayList<>(animalList);
    }

    public void addOnContentChangeListener(OnAnimalContentChangeListener listener) {
        onContentChangeListeners.add(listener);
    }

    public void addAnimal(Animal animal) {
        animalList.add(animal);
        for (OnAnimalContentChangeListener listener : onContentChangeListeners) {
            listener.onAnimalAdded(animal);
        }
    }

    public void removeOnContentChangeListener(OnAnimalContentChangeListener listener) {
        onContentChangeListeners.remove(listener);
    }


}
