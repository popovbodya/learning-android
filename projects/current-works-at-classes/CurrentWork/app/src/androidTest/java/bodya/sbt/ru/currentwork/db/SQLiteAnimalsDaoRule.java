package bodya.sbt.ru.currentwork.db;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.rules.ExternalResource;

import java.util.ArrayList;
import java.util.List;

public class SQLiteAnimalsDaoRule extends ExternalResource {

    private static final String TAG = SQLiteAnimalsDaoRule.class.getSimpleName();
    private static final String BASE_NAME = "animals_test.db";
    private SQLiteAnimalsDao sqLiteAnimalsDao;
    private Context context;

    @Override
    protected void before() throws Throwable {
        Log.e(TAG, "before");
        context = InstrumentationRegistry.getTargetContext();
        sqLiteAnimalsDao = new SQLiteAnimalsDao(context, BASE_NAME, SQLiteAnimalsDao.CURRENT_VERSION);
    }

    @Override
    protected void after() {
        Log.e(TAG, "after");
        context.deleteDatabase(BASE_NAME);
        sqLiteAnimalsDao.close();
    }

    SQLiteAnimalsDao getSqLiteAnimalsDao() {
        return sqLiteAnimalsDao;
    }

    List<String> getAllAnimalsContracts() {
        Log.e(TAG, "getAllAnimalsContracts");
        List<String> contractsList = new ArrayList<>();
        contractsList.add(AnimalsContract.Animal._ID);
        contractsList.add(AnimalsContract.Animal.NAME);
        contractsList.add(AnimalsContract.Animal.AGE);
        contractsList.add(AnimalsContract.Animal.TYPE);
        contractsList.add(AnimalsContract.Animal.HEIGHT);
        contractsList.add(AnimalsContract.Animal.WEIGHT);
        return contractsList;
    }
}
