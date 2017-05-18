package bodya.sbt.ru.currentwork;

/*
Есть список из 10 животных.
Лоадер должен из него взять случайное животное и вывести информацию о нём (биологический вид, масса, рост, кличка).
При перевороте информация должна сохраниться. При клике на кнопку Обновить -- информация должна перегрузиться
 */

import java.io.Serializable;

public class Animal implements Serializable {

    private enum AnimalType {
        Dog,
        Cat,
        Cow,
        Horse,
        Rabbit,
        Penguin,
        Owl,
        Monkey
    }

    private String name;
    private int weight;
    private int height;
    private int age;
    private AnimalType animalType;

    public Animal(String name, int age, int weight, int height) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.animalType = getRandomType();
    }


    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animal animal = (Animal) o;

        if (weight != animal.weight) return false;
        if (height != animal.height) return false;
        return name != null ? name.equals(animal.name) : animal.name == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + weight;
        result = 31 * result + height;
        return result;
    }

    private AnimalType getRandomType() {
        int index = (int) (Math.random() * AnimalType.values().length);
        return AnimalType.values()[index];
    }
}