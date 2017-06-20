package ru.boyda.popov.usergit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.boyda.popov.usergit.networking.Response;
import ru.boyda.popov.usergit.pojo.User;

public class EntitiesGenerator {

    private static final Random RANDOM = new Random();
    private static final int SIZE = 30;
    private static final int START_CHAR = (int) 'A';
    private static final int END_CHAR = (int) 'Z';

    public static Response createRandomUserResponse() {
        Response response = new Response();
        response.setTotalCount(SIZE);
        response.setUserList(createRandomUserList());
        return response;
    }

    public static List<User> createRandomUserList() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            users.add(createRandomUser());
        }
        return users;
    }

    private static User createRandomUser() {
        String name = createRandomString();
        User user = new User();
        user.setUsername(name);
        user.setUrl("https://api.github.com/users/" + name);
        user.setAvatarUrl("https://avatars3.githubusercontent.com/u/" + createRandomInt(15000000) + "?v=3");
        user.setScore(createRandomDouble());
        return user;
    }

    private static double createRandomDouble() {
        return RANDOM.nextDouble();
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
