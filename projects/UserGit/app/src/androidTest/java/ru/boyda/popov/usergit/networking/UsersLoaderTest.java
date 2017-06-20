package ru.boyda.popov.usergit.networking;


import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.usergit.EntitiesGenerator;
import ru.boyda.popov.usergit.pojo.User;
import ru.boyda.popov.usergit.storages.UsersStorage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.core.Every.everyItem;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsersLoaderTest {

    private static final String TEST_SEARCH_VALUE = "test_search_value";

    @Mock
    public UsersStorage usersStorage;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UsersLoader spyUsersLoader;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getContext();
        spyUsersLoader = spy(new UsersLoader(context, usersStorage));
    }

    @Test
    public void testLoadWithoutCache() throws IOException {
        Response randomUserResponse = EntitiesGenerator.createRandomUserResponse();
        LoadResult<User> expected = new LoadResult<>(TEST_SEARCH_VALUE, randomUserResponse.getUserList(), null);
        when(usersStorage.getLastSearchValue()).thenReturn(TEST_SEARCH_VALUE);
        doReturn(randomUserResponse).when(spyUsersLoader).getResponseWithSpecifiedValue(TEST_SEARCH_VALUE);
        LoadResult<User> actual = spyUsersLoader.loadInBackground();
        verify(usersStorage, times(1)).getLastSearchValue();
        verify(spyUsersLoader, times(1)).getResponseWithSpecifiedValue(TEST_SEARCH_VALUE);
        assertThat(actual.getResult(), everyItem(isIn(expected.getResult())));
    }

    @Test
    public void testOnLoadMore() throws IOException {
        spyUsersLoader.onLoadMore();
        verify(spyUsersLoader).forceLoad();
        verify(spyUsersLoader).setAddToCache(true);
    }

    @Test
    public void testLoadWithCache() throws IOException {
        List<User> firstLoadedUserList = EntitiesGenerator.createRandomUserList();
        LoadResult<User> firstLoadResult = new LoadResult<>(TEST_SEARCH_VALUE, firstLoadedUserList, null);
        spyUsersLoader.setCachedResult(firstLoadResult);

        when(usersStorage.getLastSearchValue()).thenReturn(TEST_SEARCH_VALUE);
        Response randomUserResponse = EntitiesGenerator.createRandomUserResponse();
        doReturn(randomUserResponse).when(spyUsersLoader).getResponseWithSpecifiedValue(TEST_SEARCH_VALUE);
        List<User> secondLoadedUserList = randomUserResponse.getUserList();

        List<User> expectedUserList = new ArrayList<>(firstLoadedUserList);
        expectedUserList.addAll(secondLoadedUserList);
        LoadResult<User> expected = new LoadResult<>(TEST_SEARCH_VALUE, expectedUserList, null);

        spyUsersLoader.setAddToCache(true);
        LoadResult<User> actual = spyUsersLoader.loadInBackground();

        assertThat(actual.getResult().size(), is(expected.getResult().size()));
        assertThat(actual.getResult(), everyItem(isIn(expected.getResult())));
    }

}
