package bodya.sbt.ru.currentwork;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class AnimalLoader extends AsyncTaskLoader<List<Animal>> implements MyApplication.OnContentChangeListener {

    private List<Animal> mCachedResult;
    private MyApplication myApplication;

    public AnimalLoader(Context context) {
        super(context);
        myApplication = (MyApplication) context.getApplicationContext();
        myApplication.addOnContentChangeListener(this);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if ((mCachedResult == null) || takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Animal> data) {
        super.deliverResult(data);
        mCachedResult = data;
    }

    @Override
    public List<Animal> loadInBackground() {
        return myApplication.getAnimalList();
    }

    @Override
    protected void onReset() {
        super.onReset();
        myApplication.removeOnContentChangeListener(this);
    }

    @Override
    public void onAnimalAdded(Animal animal) {
        onContentChanged();
    }

}
