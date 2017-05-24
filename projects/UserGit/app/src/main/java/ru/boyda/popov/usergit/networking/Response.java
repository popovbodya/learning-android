package ru.boyda.popov.usergit.networking;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import ru.boyda.popov.usergit.pojo.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    @JsonProperty("items")
    private List<User> userList;

    @JsonProperty("total_count")
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public List<User> getUserList() {
        return userList;
    }

}
