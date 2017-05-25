package ru.boyda.popov.usergit.interfaces;


import ru.boyda.popov.usergit.storages.UsersStorage;

public interface UsersStorageProvider {
    UsersStorage getUsersStorage();
}
