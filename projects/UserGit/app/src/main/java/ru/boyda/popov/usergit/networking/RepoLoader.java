package ru.boyda.popov.usergit.networking;


import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import ru.boyda.popov.usergit.pojo.Repository;
import ru.boyda.popov.usergit.storages.UsersStorage;

public class RepoLoader extends AsyncTaskLoader<LoadResult<Repository>> {

    private static final String TAG = "RepoLoader";
    private LoadResult<Repository> cachedResult;
    private UsersStorage usersStorage;

    public RepoLoader(Context context, UsersStorage usersStorage) {
        super(context);
        this.usersStorage = usersStorage;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (cachedResult == null) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(LoadResult<Repository> data) {
        Log.e(TAG, "deliverResult with data: " + data);
        super.deliverResult(data);
        cachedResult = data;
    }

    @Override
    public LoadResult<Repository> loadInBackground() {

        List<Repository> repositoryList;
        int index = usersStorage.getUserIndexInList();
        String username = usersStorage.getUsersList().get(index).getUsername();

        try {
            URL url = new URL("https://api.github.com/users/" + username + "/repos");
            ObjectMapper mapper = new ObjectMapper();
            repositoryList = Arrays.asList(mapper.readValue(url, Repository[].class));
        } catch (IOException e) {
            Log.e(TAG, "IO Exception in loadInBackground");
            return new LoadResult<>(username, null, e);
        }
        return new LoadResult<>(username, repositoryList, null);
    }
}
