package bodya.sbt.ru.currentwork.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import bodya.sbt.ru.currentwork.Animal;
import bodya.sbt.ru.currentwork.AnimalLoader;
import bodya.sbt.ru.currentwork.AnimalStorage;
import bodya.sbt.ru.currentwork.AnimalsAdapter;
import bodya.sbt.ru.currentwork.R;
import bodya.sbt.ru.currentwork.interfaces.AnimalsStorageProvider;


public class AnimalsInfoActivity extends AppCompatActivity {

    private static final int ANIMAL_ID = 0;
    private static final String TAG = "AnimalsInfoActivity";
    private static final String DELETE_STATUS = "delete_status";
    private static final String UPDATE_STATUS = "update_status";
    private static final String ANIMAL_KEY = "animal_key";

    private AnimalStorage animalStorage;
    private AnimalsAdapter adapter;

    private boolean deleteStatus = false;
    private boolean updateStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            deleteStatus = savedInstanceState.getBoolean(DELETE_STATUS);
            updateStatus = savedInstanceState.getBoolean(UPDATE_STATUS);
        }

        AnimalsStorageProvider animalsStorageProvider = (AnimalsStorageProvider) getApplication();
        animalStorage = animalsStorageProvider.getAnimalsStorage();

        adapter = new AnimalsAdapter();
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animal = adapter.getItem(position);
                if (deleteStatus) {
                    Log.e(TAG, "onItemClick with deleteMode: " + animal.getName());
                    animalStorage.deleteAnimal(animal);
                    deleteStatus = false;
                }
                if (updateStatus) {
                    Log.e(TAG, "onItemClick with updateMode: " + animal.getName());
                    Intent intent = AddNewAnimalActivity.newIntent(AnimalsInfoActivity.this);
                    intent.putExtra(ANIMAL_KEY, (Parcelable) animal);
                    startActivity(intent);
                    updateStatus = false;
                }

            }
        });

        getSupportLoaderManager().initLoader(ANIMAL_ID, null, new AnimalLoaderCallbacks());
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
            case R.id.add_animal_menu_item: {
                startActivity(AddNewAnimalActivity.newIntent(this));
                break;
            }
            case R.id.update_animal: {
                updateStatus = true;
                reverseDeleteStatus();
                break;
            }

            case R.id.delete_animal: {
                deleteStatus = true;
                reverseUpdateStatus();
                break;
            }
            default: {
                handled = super.onOptionsItemSelected(item);
                reverseDeleteStatus();
                reverseUpdateStatus();
            }
        }
        return handled;
    }

    private void reverseUpdateStatus() {
        if (updateStatus) {
            updateStatus = false;
        }
    }

    private void reverseDeleteStatus() {
        if (deleteStatus) {
            deleteStatus = false;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DELETE_STATUS, deleteStatus);
        outState.putBoolean(UPDATE_STATUS, updateStatus);
    }

    private class AnimalLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Animal>> {

        @Override
        public Loader<List<Animal>> onCreateLoader(int id, Bundle args) {
            Log.e(TAG, "onCreateLoader with: " + AnimalsInfoActivity.this);
            return new AnimalLoader(AnimalsInfoActivity.this, animalStorage);
        }

        @Override
        public void onLoadFinished(Loader<List<Animal>> loader, List<Animal> data) {
            Log.e(TAG, "onLoadFinished");
            adapter.setAnimals(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Animal>> loader) {
        }
    }
}
