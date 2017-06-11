package bodya.sbt.ru.currentwork.activities;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import bodya.sbt.ru.currentwork.Animal;
import bodya.sbt.ru.currentwork.AnimalsInfoApplication;
import bodya.sbt.ru.currentwork.AnimalStorage;
import bodya.sbt.ru.currentwork.AnimalsAdapter;
import bodya.sbt.ru.currentwork.R;
import bodya.sbt.ru.currentwork.async.DataBaseLoaderFunctions;
import bodya.sbt.ru.currentwork.async.DataBaseWorker;
import bodya.sbt.ru.currentwork.interfaces.DataBaseLoaderProvider;


public class AnimalsInfoActivity extends AppCompatActivity implements DataBaseWorker.LoaderCallback {

    private static final String TAG = "AnimalsInfoActivity";
    private static final String ANIMAL_KEY = "animal_key";
    private static final String EDIT_MODE_KEY = "edit_mode_key";

    private TextView modeTextView;

    private AnimalStorage animalStorage;
    private AnimalsAdapter adapter;
    private DataBaseWorker dataBaseWorker;
    private EditMode editMode;

    private enum EditMode implements Serializable {
        Update,
        Delete,
        View
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Application application = getApplication();

        animalStorage = ((AnimalsInfoApplication) application).getAnimalsStorage();

        DataBaseLoaderProvider dataBaseLoaderProvider = (DataBaseLoaderProvider) application;
        dataBaseWorker = dataBaseLoaderProvider.getDataBaseWorker();
        dataBaseWorker.setListener(this);

        adapter = new AnimalsAdapter();

        if (savedInstanceState != null) {
            editMode = (EditMode) savedInstanceState.getSerializable(EDIT_MODE_KEY);
            getCachedData();
        } else {
            dataBaseWorker.queueTask(DataBaseLoaderFunctions.READ_USERS);
        }

        modeTextView = (TextView) findViewById(R.id.text_view_mode);


        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animal = adapter.getItem(position);
                if (editMode == EditMode.Delete) {
                    Log.e(TAG, "onItemClick with deleteMode: " + animal.getName());
                    dataBaseWorker.queueTask(DataBaseLoaderFunctions.DELETE_USER, animal);
                }
                if (editMode == EditMode.Update) {
                    Log.e(TAG, "onItemClick with updateMode: " + animal.getName());
                    Intent intent = AddNewAnimalActivity.newIntent(AnimalsInfoActivity.this);
                    intent.putExtra(ANIMAL_KEY, animal);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (editMode == null) {
            return;
        }
        switch (editMode) {
            case View:
                modeTextView.setText(getResources().getString(R.string.view_mode));
                break;
            case Delete:
                modeTextView.setText(getResources().getString(R.string.delete_mode));
                break;
            case Update:
                modeTextView.setText(getResources().getString(R.string.update_mode));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        dataBaseWorker.setListener(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.animals_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        switch (item.getItemId()) {
            case R.id.observe_animals: {
                editMode = EditMode.View;
                modeTextView.setText(getResources().getString(R.string.view_mode));
                break;
            }
            case R.id.add_animal_menu_item: {
                startActivity(AddNewAnimalActivity.newIntent(this));
                break;
            }
            case R.id.update_animal: {
                editMode = EditMode.Update;
                modeTextView.setText(getResources().getString(R.string.update_mode));
                break;
            }
            case R.id.delete_animal: {
                editMode = EditMode.Delete;
                modeTextView.setText(getResources().getString(R.string.delete_mode));
                break;
            }
            default: {
                handled = super.onOptionsItemSelected(item);
            }
        }
        return handled;
    }

    private void getCachedData() {
        Log.e(TAG, "getCachedData");
        adapter.setAnimals(animalStorage.getCachedAnimalList());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EDIT_MODE_KEY, editMode);
    }

    @Override
    public void onLoadFinished(List<Animal> data) {
        Log.e(TAG, "onLoadFinished in AnimalsActivity");
        adapter.setAnimals(data);
    }

}
