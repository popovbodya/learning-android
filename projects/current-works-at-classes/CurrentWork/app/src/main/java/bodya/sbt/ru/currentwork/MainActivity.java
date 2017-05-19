package bodya.sbt.ru.currentwork;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.*;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements Worker.Callback {

    private static final String TAG = "MainActivity";

    private static int ANIMAL_ID = 0;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportLoaderManager().getLoader(ANIMAL_ID).forceLoad();
            }
        });

        getSupportLoaderManager().initLoader(ANIMAL_ID, null, new RatesLoaderCallbacks());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAnimalDownloaded(Animal animal) {
        MyApplication myApplication = (MyApplication) getApplication();
        if (!animal.equals(myApplication.getCache())) {
            createFragment(animal);
            myApplication.setCache(animal);
        }
    }

    private void createFragment(Animal animal) {
        AnimalInfoFragment fragment = AnimalInfoFragment.newInstance(animal);

        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_for_data, fragment)
                .commitAllowingStateLoss();
    }

    private class RatesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Animal> {
        @Override
        public Loader<Animal> onCreateLoader(int id, Bundle args) {
            return new AnimalLoader(MainActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<Animal> loader, Animal data) {
            Log.e(TAG, "onLoadFinished");
            MyApplication myApplication = (MyApplication) getApplication();
            if (!data.equals(myApplication.getCache())) {
                createFragment(data);
                myApplication.setCache(data);
            }

        }

        @Override
        public void onLoaderReset(Loader<Animal> loader) {

        }
    }
}
