package bodya.sbt.ru.currentwork;


import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private List<Animal> animalList;
    private final List<OnContentChangeListener> onContentChangeListeners;

    public MyApplication() {
        onContentChangeListeners = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();

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

    public void addOnContentChangeListener(OnContentChangeListener listener) {
        onContentChangeListeners.add(listener);
    }

    public void addAnimal(Animal animal) {
        animalList.add(animal);
        for (OnContentChangeListener listener : onContentChangeListeners) {
            listener.onAnimalAdded(animal);
        }
    }

    public void removeOnContentChangeListener(OnContentChangeListener listener) {
        onContentChangeListeners.remove(listener);
    }

    public interface OnContentChangeListener {
        void onAnimalAdded(Animal animal);
    }
}
