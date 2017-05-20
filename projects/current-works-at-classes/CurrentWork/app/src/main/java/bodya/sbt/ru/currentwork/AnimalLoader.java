package bodya.sbt.ru.currentwork;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class AnimalLoader extends AsyncTaskLoader<List<Animal>> implements AnimalStorage.OnContentChangeListener {

    private static final String TAG = "AnimalLoader";
    private List<Animal> cachedResult;
    private AnimalStorage animalStorage;

    public AnimalLoader(Context context, AnimalStorage storage) {
        super(context);
        animalStorage = storage;
        storage.addOnContentChangeListener(this);
    }

    @Override
    protected void onStartLoading() {
        Log.e(TAG, "onStartLoading");
        super.onStartLoading();
        if ((cachedResult == null) || takeContentChanged()) {
            Log.e(TAG, "onStartLoading -> force load");
            forceLoad();
        } else {
            Log.e(TAG, "onStartLoading -> deliver result");
            deliverResult(cachedResult);
        }
    }

    @Override
    public void deliverResult(List<Animal> data) {
        Log.e(TAG, "deliverResult");
        super.deliverResult(data);
        cachedResult = data;
    }

    @Override
    public List<Animal> loadInBackground() {
        Log.e(TAG, "loadInBackground");
        return animalStorage.getAnimalList();
    }

    @Override
    protected void onReset() {
        Log.e(TAG, "onReset");
        super.onReset();
        animalStorage.removeOnContentChangeListener(this);
    }

    @Override
    public void onAnimalAdded(Animal animal) {
        Log.e(TAG, "onAnimalAdded");
        onContentChanged();
    }

}
