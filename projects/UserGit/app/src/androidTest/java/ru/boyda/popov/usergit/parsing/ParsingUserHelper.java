package ru.boyda.popov.usergit.parsing;


import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.usergit.pojo.User;

class ParsingUserHelper {

    static List<User> createMultiplySpecifiedUsers() {
        List<User> userList = new ArrayList<>();

        userList.add(createOneSpecifiedUser("moonligthshutdown", 12.183419,
                "https://api.github.com/users/moonligthshutdown", "https://avatars1.githubusercontent.com/u/24711993?v=3"));

        userList.add(createOneSpecifiedUser("moonligth", 8.706387,
                "https://api.github.com/users/moonligth", "https://avatars3.githubusercontent.com/u/14326681?v=3"));

        userList.add(createOneSpecifiedUser("Moonligths", 7.6048455,
                "https://api.github.com/users/Moonligths", "https://avatars3.githubusercontent.com/u/10499530?v=3"));

        return userList;
    }

     static User createOneSpecifiedUser(String username, double score, String url, String avatarUrl) {
        User user = new User();
        user.setUsername(username);
        user.setScore(score);
        user.setUrl(url);
        user.setAvatarUrl(avatarUrl);
        return user;
    }
}
