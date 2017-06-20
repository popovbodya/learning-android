package ru.boyda.popov.usergit.storages;

import java.util.Collection;
import java.util.Iterator;


class TestHelper {

    static void removeAll(Collection<?> collection) {
        Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
}
