package ru.boyda.popov.usergit.networking;


import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ru.boyda.popov.usergit.pojo.Repository;
import ru.boyda.popov.usergit.storages.UsersStorage;

public class RepoLoader extends AsyncTaskLoader<LoadResult<Repository>> {

    private static final String API_QUERY_FORMAT = "https://api.github.com/users/%s/repos";

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
        super.deliverResult(data);
        cachedResult = data;
    }

    @Override
    public LoadResult<Repository> loadInBackground() {

        List<Repository> repositoryList;
        int index = usersStorage.getUserIndexInList();
        String username = usersStorage.getUsersList().get(index).getUsername();

        try {
            URL url = new URL(String.format(Locale.ENGLISH, API_QUERY_FORMAT, username));
            ObjectMapper mapper = new ObjectMapper();
            repositoryList = Arrays.asList(mapper.readValue(url, Repository[].class));
        } catch (IOException e) {
            return new LoadResult<>(username, null, e);
        }
        return new LoadResult<>(username, repositoryList, null);
    }
}
