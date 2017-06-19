package bodya.sbt.ru.currentwork;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntitiesGenerator {
    private static final int SIZE = 20;
    private static final int START_CHAR = (int) 'A';
    private static final int END_CHAR = (int) 'Z';
    private static final Random RANDOM = new Random();

    public static Animal createRandomAnimal() {
        String name = createRandomString();
        int age = createRandomInt();
        int weight = createRandomInt();
        int height = createRandomInt();
        return new Animal(name, age, weight, height);
    }

    public static List<Animal> createRandomAnimalsList() {
        List<Animal> animals = new ArrayList<>();
        int size = createRandomInt(SIZE) + 1;
        for (int i = 0; i < size; i++) {
            animals.add(createRandomAnimal());
        }
        return animals;
    }

    static long createRandomLong() {
        return RANDOM.nextLong();
    }

    private static int createRandomInt() {
        return RANDOM.nextInt();
    }

    private static int createRandomInt(int max) {
        return RANDOM.nextInt(max);
    }

    private static String createRandomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            int value = START_CHAR + RANDOM.nextInt(
                    END_CHAR - START_CHAR
            );
            sb.append((char) value);
        }
        return sb.toString();
    }
}
