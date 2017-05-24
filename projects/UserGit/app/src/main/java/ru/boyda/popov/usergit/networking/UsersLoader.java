package ru.boyda.popov.usergit.networking;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.usergit.interfaces.OnLoadMoreListener;
import ru.boyda.popov.usergit.pojo.User;
import ru.boyda.popov.usergit.storages.UsersStorage;

public class UsersLoader extends AsyncTaskLoader<LoadResult<User>> implements OnLoadMoreListener {

    private static final String TAG = "UsersLoader";
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private LoadResult<User> cachedResult;
    private UsersStorage usersStorage;
    private boolean addToCache;

    public UsersLoader(Context context, UsersStorage storage) {
        super(context);
        usersStorage = storage;
        usersStorage.addOnLoadMoreListener(this);
        usersStorage.setPageCounter(DEFAULT_PAGE_NUMBER);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged() || (cachedResult == null)) {
            if (usersStorage.getLastSearchValue() != null) {
                addToCache = false;
                forceLoad();
            }
        }
    }

    @Override
    public void deliverResult(LoadResult<User> data) {
        Log.e(TAG, "deliverResult with data: " + data);
        super.deliverResult(data);
        cachedResult = data;
    }

    @Override
    public LoadResult<User> loadInBackground() {
        Response response;
        String searchValue = usersStorage.getLastSearchValue();

        if (cachedResult != null && cachedResult.getSearchValue().equals(searchValue) && !addToCache && cachedResult.getException() == null) {
            Log.e(TAG, "loadInBackground + used cached result");
            return new LoadResult<>(cachedResult);
        }

        try {
            Log.e(TAG, "loadInBackground");
            response = getResponseWithSpecifiedValue(searchValue);
        } catch (IOException e) {
            Log.e(TAG, "IO Exception in loadInBackground");
            return new LoadResult<>(searchValue, null, e);
        }
        if (addToCache) {
            addToCache = false;
            List<User> updatedList = new ArrayList<>(cachedResult.getResult());
            updatedList.addAll(response.getUserList());
            return new LoadResult<>(searchValue, updatedList, null);
        }
        return new LoadResult<>(searchValue, response.getUserList(), null);
    }


    private Response getResponseWithSpecifiedValue(String value) throws IOException {
        if (addToCache) {
            usersStorage.incrementCounter();
        } else {
            usersStorage.setPageCounter(DEFAULT_PAGE_NUMBER);
        }
        URL url = new URL("https://api.github.com/search/users?q=" + value + "&page=" + usersStorage.getPageCounter());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(url, Response.class);
    }

    @Override
    protected void onReset() {
        super.onReset();
        usersStorage.removeOnLoadMoreListener(this);
    }


    @Override
    public void onLoadMore() {
        addToCache = true;
        forceLoad();
    }
}
