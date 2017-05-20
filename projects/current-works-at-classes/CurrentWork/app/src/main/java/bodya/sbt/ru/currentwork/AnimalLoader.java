package bodya.sbt.ru.currentwork;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import bodya.sbt.ru.currentwork.interfaces.OnAnimalContentChangeListener;

public class AnimalLoader extends AsyncTaskLoader<List<Animal>> implements OnAnimalContentChangeListener {

    private List<Animal> cachedResult;
    private AnimalStorage animalStorage;

    public AnimalLoader(Context context, AnimalStorage storage) {
        super(context);
        animalStorage = storage;
        storage.addOnContentChangeListener(this);
    }

    @Override
    protected void onStartLoading() {
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
        return animalStorage.getAnimalList();
    }

    @Override
    protected void onReset() {
        super.onReset();
        animalStorage.removeOnContentChangeListener(this);
    }

    @Override
    public void onAnimalAdded(Animal animal) {
        onContentChanged();
    }

}
