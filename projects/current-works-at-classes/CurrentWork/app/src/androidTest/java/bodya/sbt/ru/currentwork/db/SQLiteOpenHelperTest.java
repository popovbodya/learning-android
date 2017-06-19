package bodya.sbt.ru.currentwork.db;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class SQLiteOpenHelperTest {

    @Rule
    public SQLiteAnimalsDaoRule daoRule = new SQLiteAnimalsDaoRule();

    @Test
    public void testDbCreation() {
        List<String> expected = daoRule.getAllAnimalsContracts();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = daoRule.getSqLiteAnimalsDao().getReadableDatabase();
            assertThat(db, notNullValue());
            cursor = db.query(SQLiteAnimalsDao.TABLE_NAME, null, null, null, null, null, null);
            assertThat(cursor, notNullValue());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        List<String> actual = Arrays.asList(cursor.getColumnNames());
        assertThat(actual, everyItem(isIn(expected)));
    }
}
