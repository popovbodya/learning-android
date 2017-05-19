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


/*
Есть список из 10 животных.
Лоадер должен из него взять случайное животное и вывести информацию о нём (биологический вид, масса, рост, кличка).
При перевороте информация должна сохраниться. При клике на кнопку Обновить -- информация должна перегрузиться
 */

public class MainActivity extends FragmentActivity implements Worker.Callback {

    private static final String TAG = "MainActivity";
<<<<<<< HEAD
=======
    private static int ANIMAL_ID = 0;
    private Worker worker;
>>>>>>> b19a674d9a9a143a9f5bbb9b84fa21f6280a2b10
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
<<<<<<< HEAD
                getSupportLoaderManager().getLoader(0).forceLoad();
=======
//                worker.queueTask();
                getSupportLoaderManager().getLoader(ANIMAL_ID).forceLoad();
>>>>>>> b19a674d9a9a143a9f5bbb9b84fa21f6280a2b10
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
