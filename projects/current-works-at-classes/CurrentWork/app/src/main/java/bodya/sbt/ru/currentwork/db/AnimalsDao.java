package bodya.sbt.ru.currentwork.db;


import java.util.List;

import bodya.sbt.ru.currentwork.Animal;

public interface AnimalsDao {

    long insertAnimal(Animal animal);

    List<Animal> getAnimals();

    Animal getAnimalById(long id);

    int updateAnimal(Animal animal);

    int deleteAnimal(Animal animal);
}
