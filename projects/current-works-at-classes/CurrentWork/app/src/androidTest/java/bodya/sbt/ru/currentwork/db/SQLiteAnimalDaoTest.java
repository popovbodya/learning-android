package bodya.sbt.ru.currentwork.db;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import bodya.sbt.ru.currentwork.Animal;
import bodya.sbt.ru.currentwork.EntitiesGenerator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.*;

public class SQLiteAnimalDaoTest {

    private static final String TAG = SQLiteAnimalDaoTest.class.getSimpleName();

    private final SQLiteAnimalsDaoRule daoRule = new SQLiteAnimalsDaoRule();
    private final ExpectedException expectedException = ExpectedException.none();
    private final TestName testName = new TestName();

    @Rule
    public TestRule rule = RuleChain
            .outerRule(daoRule)
            .around(expectedException)
            .around(testName);


    @Test
    public void dbCreationTest() {
        Log.e(TAG, testName.getMethodName());
        List<String> expected = daoRule.getAllAnimalsContracts();
        SQLiteDatabase db = daoRule.getSqLiteAnimalsDao().getReadableDatabase();
        Cursor cursor = db.query(SQLiteAnimalsDao.TABLE_NAME, null, null, null, null, null, null);
        cursor.close();
        List<String> actual = Arrays.asList(cursor.getColumnNames());
        assertThat(actual, everyItem(isIn(expected)));
    }

    @Test
    public void testInsert() {
        Log.e(TAG, testName.getMethodName());
        Animal animal = EntitiesGenerator.createRandomAnimal();
        long id = daoRule.getSqLiteAnimalsDao().insertAnimal(animal);
        assertThat(true, is(id > 0));
    }

    @Test
    public void testInsertNull() {
        Log.e(TAG, testName.getMethodName());
        Animal animal = null;
        expectedException.expect(NullPointerException.class);
        daoRule.getSqLiteAnimalsDao().insertAnimal(animal);
    }

    @Test
    public void testGetAnimals() {
        Log.e(TAG, testName.getMethodName());
        List<Animal> expected = EntitiesGenerator.createRandomAnimalsList();
        for (Animal animal : expected) {
            long id = daoRule.getSqLiteAnimalsDao().insertAnimal(animal);
            animal.setId(id);
        }
        List<Animal> actual = daoRule.getSqLiteAnimalsDao().getAnimals();
        assertThat(actual, everyItem(isIn(expected)));
    }

    @Test
    public void testGetAnimalById() {
        Log.e(TAG, testName.getMethodName());
        Animal expected = EntitiesGenerator.createRandomAnimal();
        long id = daoRule.getSqLiteAnimalsDao().insertAnimal(expected);
        expected.setId(id);
        Animal actual = daoRule.getSqLiteAnimalsDao().getAnimalById(id);
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetAnimalByUnknownId() {
        Log.e(TAG, testName.getMethodName());
        long id = -1;
        Animal actual = daoRule.getSqLiteAnimalsDao().getAnimalById(id);
        assertThat(actual, is(nullValue()));
    }


    @Test
    public void testUpdateAnimal() {
        Log.e(TAG, testName.getMethodName());
        Animal expected = EntitiesGenerator.createRandomAnimal();
        long id = daoRule.getSqLiteAnimalsDao().insertAnimal(expected);
        expected.setId(id);
        expected.setAge(expected.hashCode());
        expected.setHeight(expected.hashCode());
        daoRule.getSqLiteAnimalsDao().updateAnimal(expected);
        Animal actual = daoRule.getSqLiteAnimalsDao().getAnimalById(id);
        assertThat(actual, is(expected));
    }

    @Test
    public void testUpdateNullAnimal() {
        Log.e(TAG, testName.getMethodName());
        Animal animal = null;
        expectedException.expect(NullPointerException.class);
        daoRule.getSqLiteAnimalsDao().updateAnimal(animal);
    }

}
