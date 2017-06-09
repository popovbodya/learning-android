package bodya.sbt.ru.currentwork;

import java.io.Serializable;

public class Animal implements Serializable {

    public enum AnimalType {
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
    private AnimalType animalType;
    private int weight;
    private int height;
    private int age;
    private long id;

    public Animal() {
    }

    public Animal(String name, int age, int weight, int height) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.animalType = getRandomType();
    }

    public String getRandomAnimalType() {
        return getRandomType().toString();
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

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animal animal = (Animal) o;

        if (weight != animal.weight) return false;
        if (height != animal.height) return false;
        if (age != animal.age) return false;
        if (id != animal.id) return false;
        if (name != null ? !name.equals(animal.name) : animal.name != null) return false;
        return animalType == animal.animalType;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (animalType != null ? animalType.hashCode() : 0);
        result = 31 * result + weight;
        result = 31 * result + height;
        result = 31 * result + age;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", animalType=" + animalType +
                ", weight=" + weight +
                ", height=" + height +
                ", age=" + age +
                ", id=" + id +
                '}';
    }

    private AnimalType getRandomType() {
        int index = (int) (Math.random() * AnimalType.values().length);
        return AnimalType.values()[index];
    }
}
