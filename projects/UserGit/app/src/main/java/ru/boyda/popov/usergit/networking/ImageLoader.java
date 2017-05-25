package ru.boyda.popov.usergit.networking;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ru.boyda.popov.usergit.storages.UsersStorage;

public class ImageLoader extends AsyncTaskLoader<Drawable> {

    private static final String SRC_NAME = "avatar";

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

        int index = usersStorage.getUserIndexInList();
        String url = usersStorage.getUsersList().get(index).getAvatarUrl();
        Drawable drawable = null;
        InputStream is = null;
        try {
            is = (InputStream) new URL(url).getContent();
            drawable = Drawable.createFromStream(is, SRC_NAME);
        } catch (Exception ignored) {
        } finally {
            closeStream(is);
        }
        return drawable;
    }

    private void closeStream(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
