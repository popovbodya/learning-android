package ru.dimasokol.learning.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class LoggingActivity extends Activity {

    private LoggingStorage mStorage;
    private LoggingAdapter mAdapter;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        mListView = (ListView) findViewById(R.id.messages_list);
        mStorage = ((ActivitySampleApplication) getApplication()).getStorage();

        mStorage.addMessage("onCreate");

        mAdapter = new LoggingAdapter(mStorage);
        mListView.setAdapter(mAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mStorage.addMessage("onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();

        mStorage.addMessage("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();

        mStorage.addMessage("onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStorage.addMessage("onResume");
        mAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStorage.addMessage("onPause");
        mAdapter.stopListening();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mStorage.addMessage("onRestart");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mStorage.addMessage("onWindowFocusChanged / " + Boolean.toString(hasFocus));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStorage.addMessage("onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStorage.addMessage("onSaveInstanceState");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mStorage.addMessage("onNewIntent");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mStorage.addMessage("onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mStorage.addMessage("onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        mStorage.addMessage("onTrimMemory");
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        mStorage.addMessage("onRetainNonConfigurationInstance");
        return super.onRetainNonConfigurationInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mStorage.addMessage("onCreateOptionsMenu");
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_logger, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mStorage.addMessage("onPrepareOptionsMenu");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mStorage.addMessage("onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.action_clear:
                mStorage.clear();
                return true;
            case R.id.action_run_activity:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
