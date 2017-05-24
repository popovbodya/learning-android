package ru.boyda.popov.usergit.activities;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.boyda.popov.usergit.R;
import ru.boyda.popov.usergit.adapters.RepoListAdapter;
import ru.boyda.popov.usergit.interfaces.UsersStorageProvider;
import ru.boyda.popov.usergit.networking.ImageLoader;
import ru.boyda.popov.usergit.networking.LoadResult;
import ru.boyda.popov.usergit.networking.RepoLoader;
import ru.boyda.popov.usergit.pojo.Repository;
import ru.boyda.popov.usergit.pojo.User;
import ru.boyda.popov.usergit.storages.UsersStorage;

public class UserOverviewActivity extends AppCompatActivity {

    private static final String USER_INDEX_KEY = "user_index";
    private static final int REPO_DOWNLOAD_ID = 0;
    private static final int IMAGE_DOWNLOAD_ID = 1;

    private ImageView userImageView;
    private ProgressBar progressBar;
    private ListView listView;
    private View errorLayout;

    private UsersStorage usersStorage;
    private RepoListAdapter repoListAdapter;
    private int userIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_overview);

        UsersStorageProvider usersStorageProvider = (UsersStorageProvider) getApplication();
        usersStorage = usersStorageProvider.getUsersStorage();

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                userIndex = bundle.getInt(USER_INDEX_KEY);
                usersStorage.setUserIndexInList(userIndex);
            }
        } else {
            userIndex = savedInstanceState.getInt(USER_INDEX_KEY);
            usersStorage.setUserIndexInList(userIndex);
        }

        User user = usersStorage.getUsersList().get(usersStorage.getUserIndexInList());

        userImageView = (ImageView) findViewById(R.id.user_overview_icon);
        TextView userLoginTextView = (TextView) findViewById(R.id.login_overview);
        userLoginTextView.setText(user.getUsername());
        TextView userUrlTextView = (TextView) findViewById(R.id.url_overview);
        userUrlTextView.setText(user.getUrl());
        TextView messageAboutReps = (TextView) findViewById(R.id.message_about_reps_overview);
        messageAboutReps.setText(getString(R.string.list_of_user_repositories, user.getUsername()));

        errorLayout = findViewById(R.id.layout_error_overview);
        Button retryButton = (Button) findViewById(R.id.try_again_button_overview);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToLoad();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_overview);

        listView = (ListView) findViewById(R.id.list_view_overview);
        repoListAdapter = new RepoListAdapter();
        listView.setAdapter(repoListAdapter);

        getSupportLoaderManager().initLoader(REPO_DOWNLOAD_ID, null, new RepoLoaderCallbacks());
        getSupportLoaderManager().initLoader(IMAGE_DOWNLOAD_ID, null, new ImageLoaderCallbacks());

        showError(false);
        showProgress(true);

    }

    private void tryToLoad() {
        showProgress(true);
        showError(false);
        showLoadedResult(false);
        getSupportLoaderManager().getLoader(REPO_DOWNLOAD_ID).forceLoad();
        getSupportLoaderManager().getLoader(IMAGE_DOWNLOAD_ID).forceLoad();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(USER_INDEX_KEY, userIndex);
    }

    private void showLoadedResult(boolean show) {
        listView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(boolean show) {
        errorLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private class RepoLoaderCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<Repository>> {

        @Override
        public Loader<LoadResult<Repository>> onCreateLoader(int id, Bundle args) {
            return new RepoLoader(UserOverviewActivity.this, usersStorage);
        }

        @Override
        public void onLoadFinished(Loader<LoadResult<Repository>> loader, LoadResult<Repository> data) {
            showProgress(false);
            showLoadedResult(true);
            if (data.getException() != null) {
                showError(true);
                return;
            }
            List<Repository> loadedList = data.getResult();
            repoListAdapter.setUserList(loadedList);
        }

        @Override
        public void onLoaderReset(Loader<LoadResult<Repository>> loader) {
        }
    }

    private class ImageLoaderCallbacks implements LoaderManager.LoaderCallbacks<Drawable> {

        @Override
        public Loader<Drawable> onCreateLoader(int id, Bundle args) {
            return new ImageLoader(UserOverviewActivity.this, usersStorage);
        }

        @Override
        public void onLoadFinished(Loader<Drawable> loader, Drawable data) {
            if (data != null) {
                userImageView.setImageDrawable(data);
            } else {
                userImageView.setImageResource(R.drawable.profile_icon);
            }
        }

        @Override
        public void onLoaderReset(Loader<Drawable> loader) {
        }
    }
}
