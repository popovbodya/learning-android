package ru.popov.bodya.gitwatcher;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class UserStorage {
    private Set<String> userSet;
    private List<OnContentChangedListener> listeners;

    public UserStorage() {
        userSet = new TreeSet<>();
        listeners = new ArrayList<>();
    }

    public void addNewUser(String username) {
        userSet.add(username);
        notifyListeners();
    }

    public void deleteUser(String username) {
        userSet.remove(username);
        notifyListeners();
    }

    public Set<String> getUserSet() {
        return userSet;
    }

    public interface OnContentChangedListener {
        void onContentChanged(Set<String> usernameSet);
    }

    public void addNewListener(OnContentChangedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(OnContentChangedListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (OnContentChangedListener listener: listeners) {
            listener.onContentChanged(userSet);
        }
    }

}
