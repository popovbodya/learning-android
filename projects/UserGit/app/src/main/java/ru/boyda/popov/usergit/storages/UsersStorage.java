package ru.boyda.popov.usergit.storages;


import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.usergit.interfaces.OnLoadMoreListener;
import ru.boyda.popov.usergit.pojo.User;

public class UsersStorage {

    private List<User> usersList;
    private String lastSearchValue;
    private int userIndexInList;
    private int pageCounter;
    private final List<OnLoadMoreListener> onLoadMoreListeners;

    public UsersStorage() {
        usersList = new ArrayList<>();
        onLoadMoreListeners = new ArrayList<>();
    }

    public List<User> getUsersList() {
        return new ArrayList<>(usersList);
    }

    public String getLastSearchValue() {
        return lastSearchValue;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public void setLastSearchValue(String lastSearchValue) {
        this.lastSearchValue = lastSearchValue;
    }

    public void addOnLoadMoreListener(OnLoadMoreListener listener) {
        onLoadMoreListeners.add(listener);
    }

    public void removeOnLoadMoreListener(OnLoadMoreListener listener) {
        onLoadMoreListeners.remove(listener);
    }

    public void notifyOnLoadMoreListeners() {
        for (OnLoadMoreListener onLoadMoreListener : onLoadMoreListeners) {
            onLoadMoreListener.onLoadMore();
        }
    }

    public int getUserIndexInList() {
        return userIndexInList;
    }

    public void setUserIndexInList(int userIndexInList) {
        this.userIndexInList = userIndexInList;
    }

    public int getPageCounter() {
        return pageCounter;
    }

    public void setPageCounter(int pageCounter) {
        this.pageCounter = pageCounter;
    }

    public void incrementCounter() {
        pageCounter++;
    }
}
