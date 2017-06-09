package bodya.sbt.ru.currentwork.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bodya.sbt.ru.currentwork.Animal;


public class SQLiteAnimalsDao extends SQLiteOpenHelper implements AnimalsDao {

    private static final String TAG = SQLiteAnimalsDao.class.getName();

    private static final String NAME = "animals.db";
    private static final long NO_ID = -1;
    private static final short ROW_UPDATE_STATUS = 0;
    private static final short ROW_COUNT_UPDATED = 0;
    private static final int CURRENT_VERSION = 1;

    public static final String TABLE_NAME = "animals";

    public SQLiteAnimalsDao(Context context) {
        this(context, NAME, CURRENT_VERSION);
    }

    public SQLiteAnimalsDao(Context context, String name, int version) {
        super(context, name, null, version, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                AnimalsContract.Animal._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AnimalsContract.Animal.NAME + " TEXT NOT NULL, " +
                AnimalsContract.Animal.AGE + " INTEGER NOT NULL, " +
                AnimalsContract.Animal.TYPE + " TEXT NOT NULL, " +
                AnimalsContract.Animal.WEIGHT + " INTEGER NOT NULL, " +
                AnimalsContract.Animal.HEIGHT + " INTEGER NOT NULL " +
                ");";
        Log.e(TAG, "sql = " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public long insertAnimal(Animal animal) {
        long id = NO_ID;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = createValuesFromAnimal(animal);
            id = db.insert(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return id;
    }

    @Override
    public List<Animal> getAnimals() {
        List<Animal> animals = null;
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            animals = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                animals.add(createAnimal(cursor));
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return animals;
    }

    @Override
    public Animal getAnimalById(long id) {
        Animal animal = null;
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();
        try {
            cursor = db.query(TABLE_NAME, null, AnimalsContract.Animal._ID + "=" + id, null, null, null, null
            );
            cursor.moveToFirst();
            if (cursor.isFirst()) {
                animal = createAnimal(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return animal;
    }

    @Override
    public int updateAnimal(Animal animal) {
        int rowUpdated = ROW_COUNT_UPDATED;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            rowUpdated = db.update(TABLE_NAME, createValuesFromAnimal(animal), AnimalsContract.Animal._ID + " = " + animal.getId(), null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return rowUpdated;
    }

    @Override
    public int deleteAnimal(Animal animal) {
        int isAnimalDeleted = ROW_UPDATE_STATUS;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            isAnimalDeleted = db.delete(TABLE_NAME, AnimalsContract.Animal._ID + " = " + animal.getId(), null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return isAnimalDeleted;
    }

    private static Animal createAnimal(Cursor cursor) {
        Animal animal = new Animal();
        animal.setId(getLong(cursor, AnimalsContract.Animal._ID));
        animal.setName(getString(cursor, AnimalsContract.Animal.NAME));
        animal.setAge(getInt(cursor, AnimalsContract.Animal.AGE));
        animal.setAnimalType(bodya.sbt.ru.currentwork.Animal.AnimalType.valueOf(getString(cursor, AnimalsContract.Animal.TYPE)));
        animal.setWeight(getInt(cursor, AnimalsContract.Animal.WEIGHT));
        animal.setHeight(getInt(cursor, AnimalsContract.Animal.HEIGHT));
        return animal;
    }

    private static ContentValues createValuesFromAnimal(Animal animal) {
        ContentValues values = new ContentValues();
        values.put(AnimalsContract.Animal.NAME, animal.getName());
        values.put(AnimalsContract.Animal.AGE, animal.getAge());
        values.put(AnimalsContract.Animal.TYPE, animal.getAnimalType().toString());
        values.put(AnimalsContract.Animal.WEIGHT, animal.getWeight());
        values.put(AnimalsContract.Animal.HEIGHT, animal.getHeight());
        return values;
    }

    private static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    private static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}