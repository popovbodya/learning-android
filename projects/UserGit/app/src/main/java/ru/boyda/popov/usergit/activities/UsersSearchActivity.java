package ru.boyda.popov.usergit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.usergit.R;
import ru.boyda.popov.usergit.networking.LoadResult;
import ru.boyda.popov.usergit.pojo.User;
import ru.boyda.popov.usergit.adapters.UsersListAdapter;
import ru.boyda.popov.usergit.storages.UsersStorage;
import ru.boyda.popov.usergit.interfaces.UsersStorageProvider;
import ru.boyda.popov.usergit.networking.UsersLoader;

public class UsersSearchActivity extends AppCompatActivity {

    private static final int USER_DOWNLOAD_ID = 0;
    private static final String SEARCH_VALUES_KEY = "search_values";
    private static final String USER_INDEX_KEY = "user_index";

    private AutoCompleteTextView autoCompleteTextView;
    private ProgressBar progressBar;
    private View errorLayout;
    private TextView noUsersTextView;
    private ListView listView;
    private Button mSearchButton;

    private ArrayAdapter<String> autoCompleteAdapter;
    private UsersStorage usersStorage;
    private UsersListAdapter usersListAdapter;
    private ArrayList<String> searchValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_users);

        if (savedInstanceState == null) {
            searchValues = new ArrayList<>();
        } else {
            searchValues = savedInstanceState.getStringArrayList(SEARCH_VALUES_KEY);
        }

        UsersStorageProvider usersStorageProvider = (UsersStorageProvider) getApplication();
        usersStorage = usersStorageProvider.getUsersStorage();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorLayout = findViewById(R.id.layout_error);
        noUsersTextView = (TextView) findViewById(R.id.no_users_found_view);

        Button retryButton = (Button) findViewById(R.id.try_again_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToLoad();
            }
        });

        listView = (ListView) findViewById(R.id.users_list);
        usersListAdapter = new UsersListAdapter();
        listView.setAdapter(usersListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UsersSearchActivity.this, UserOverviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(USER_INDEX_KEY, position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_complete_text_view);
        autoCompleteAdapter = new ArrayAdapter<>(UsersSearchActivity.this, R.layout.dropdown_layout, searchValues);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.addTextChangedListener(new EditTextWatcherImpl());

        mSearchButton = (Button) findViewById(R.id.search_button);
        mSearchButton.setEnabled(false);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAdd = autoCompleteTextView.getText().toString();
                if (!searchValues.contains(newAdd)) {
                    searchValues.add(newAdd);
                    autoCompleteAdapter = new ArrayAdapter<>(UsersSearchActivity.this, R.layout.dropdown_layout, searchValues);
                    autoCompleteTextView.setAdapter(autoCompleteAdapter);
                }
                tryToLoad();
                usersStorage.setLastSearchValue(newAdd);
            }
        });

        showError(false);
        showProgress(false);
        showNoUsersFoundResult(false);

        getSupportLoaderManager().initLoader(USER_DOWNLOAD_ID, null, new UsersLoaderCallbacks());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SEARCH_VALUES_KEY, searchValues);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        switch (item.getItemId()) {
            case R.id.load_more_users: {
                usersStorage.notifyOnLoadMoreListeners();
                break;
            }
            default: {
                handled = super.onOptionsItemSelected(item);
            }
        }
        return handled;
    }

    private void tryToLoad() {
        showProgress(true);
        showError(false);
        showNoUsersFoundResult(false);
        getSupportLoaderManager().getLoader(USER_DOWNLOAD_ID).forceLoad();
    }

    private void showLoadedResult(boolean show) {
        listView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showNoUsersFoundResult(boolean show) {
        noUsersTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(boolean show) {
        errorLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private class UsersLoaderCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<User>> {

        @Override
        public Loader<LoadResult<User>> onCreateLoader(int id, Bundle args) {
            return new UsersLoader(UsersSearchActivity.this, usersStorage);
        }

        @Override
        public void onLoadFinished(Loader<LoadResult<User>> loader, LoadResult<User> data) {
            showProgress(false);

            if (data.getException() != null) {
                showError(true);
                return;
            }

            List<User> loadedList = data.getResult();
            usersListAdapter.setUserList(loadedList);
            usersStorage.setUsersList(loadedList);

            if (loadedList.size() == 0) {
                showNoUsersFoundResult(true);
            }

            showLoadedResult(true);

        }

        @Override
        public void onLoaderReset(Loader<LoadResult<User>> loader) {
        }
    }

    private class EditTextWatcherImpl implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean setEnabled = false;
            if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                setEnabled = true;
            }
            mSearchButton.setEnabled(setEnabled);
        }
    }

}
