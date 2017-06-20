package ru.boyda.popov.usergit.storages;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


import ru.boyda.popov.usergit.interfaces.OnLoadMoreListener;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UsersStorageTest {

    private static final byte LISTENER_LIST_SIZE = 5;

    @Mock
    public OnLoadMoreListener onLoadMoreListener;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UsersStorage usersStorage;


    @Before
    public void prepare() {
        usersStorage = new UsersStorage();
        for (int i = 0; i < LISTENER_LIST_SIZE; i++) {
            usersStorage.addOnLoadMoreListener(onLoadMoreListener);
        }
    }

    @After
    public void tearDown() {
        TestHelper.removeAll(usersStorage.getOnLoadMoreListeners());
    }

    @Test
    public void testNotifyOnLoadMoreListeners() {
        usersStorage.notifyOnLoadMoreListeners();
        for (OnLoadMoreListener mockedListener: usersStorage.getOnLoadMoreListeners()) {
            verify(mockedListener, times(LISTENER_LIST_SIZE)).onLoadMore();
        }
    }

    @Test
    public void testIncrementCounter() {
        int expected = usersStorage.getPageCounter() + 1;
        usersStorage.incrementCounter();
        int actual = usersStorage.getPageCounter();
        assertThat(actual, is(expected));
    }

    @Test
    public void testAddOnLoadMoreListener() {
        int expected = usersStorage.getOnLoadMoreListeners().size() + 1;
        usersStorage.addOnLoadMoreListener(onLoadMoreListener);
        int actual = usersStorage.getOnLoadMoreListeners().size();
        assertThat(actual, is(expected));
    }

    @Test
    public void testRemoveOnLoadMoreListener() {
        int expected = usersStorage.getOnLoadMoreListeners().size() - 1;
        usersStorage.removeOnLoadMoreListener(onLoadMoreListener);
        int actual = usersStorage.getOnLoadMoreListeners().size();
        assertThat(actual, is(expected));
    }
}