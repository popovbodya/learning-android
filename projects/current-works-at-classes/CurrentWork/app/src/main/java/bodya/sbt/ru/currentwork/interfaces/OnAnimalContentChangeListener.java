package bodya.sbt.ru.currentwork.interfaces;

import java.util.List;

import bodya.sbt.ru.currentwork.Animal;

public interface OnAnimalContentChangeListener {
    void onContentChanged(List<Animal> animals);
}
