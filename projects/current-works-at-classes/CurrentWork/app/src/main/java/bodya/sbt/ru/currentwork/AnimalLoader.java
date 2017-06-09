package bodya.sbt.ru.currentwork;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

import bodya.sbt.ru.currentwork.interfaces.OnAnimalContentChangeListener;

public class AnimalLoader extends AsyncTaskLoader<List<Animal>> implements OnAnimalContentChangeListener {

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
        Log.e(TAG, "onStartLoading:  " + getContext());
        super.onStartLoading();
        if ((cachedResult == null) || takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Animal> data) {
        super.deliverResult(data);
        cachedResult = data;
    }

    @Override
    public List<Animal> loadInBackground() {
        Log.e(TAG, "loadInBackground:  " + getContext());
        return animalStorage.getAnimalList();
    }

    @Override
    protected void onReset() {
        super.onReset();
        animalStorage.removeOnContentChangeListener(this);
    }

    @Override
    public void onContentChanged(Animal animal) {
        onContentChanged();
    }

}
