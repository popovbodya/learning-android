package bodya.sbt.ru.currentwork;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class AnimalLoader extends AsyncTaskLoader<Animal> {

    private static final String TAG = "AnimalLoader";

    public AnimalLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.e(TAG, "onStartLoading");
    }

    @Override
    public Animal loadInBackground() {
        Log.e(TAG, "loadInBackGround");
        List<Animal> list = ((MyApplication) getContext()).getAnimalList();
        int index = (int) (Math.random() * list.size());
        return list.get(index);
    }
}
