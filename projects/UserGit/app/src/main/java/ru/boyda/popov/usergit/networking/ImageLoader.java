package ru.boyda.popov.usergit.networking;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.AsyncTaskLoader;

import java.io.InputStream;
import java.net.URL;

import ru.boyda.popov.usergit.storages.UsersStorage;

public class ImageLoader extends AsyncTaskLoader<Drawable> {

    private Drawable cachedResult;
    private UsersStorage usersStorage;

    public ImageLoader(Context context, UsersStorage usersStorage) {
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
    public void deliverResult(Drawable data) {
        super.deliverResult(data);
        cachedResult = data;
    }

    @Override
    public Drawable loadInBackground() {

        String url = usersStorage.getUsersList().get(usersStorage.getUserIndexInList()).getAvatarUrl();
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }
}
