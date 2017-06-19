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

    static final int CURRENT_VERSION = 1;
    static final String TABLE_NAME = "animals";
    private static final String NAME = "animals.db";
    private static final short ROW_UPDATE_STATUS = 0;
    private static final short ROW_COUNT_UPDATED = 0;
    private static final long NO_ID = -1;

    public SQLiteAnimalsDao(Context context) {
        this(context, NAME, CURRENT_VERSION);
    }

    SQLiteAnimalsDao(Context context, String name, int version) {
        super(context, name, null, version, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                AnimalsContract.Animal._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AnimalsContract.Animal.NAME + " TEXT NOT NULL, " +
                AnimalsContract.Animal.AGE + " INTEGER NOT NULL, " +
                AnimalsContract.Animal.TYPE + " INTEGER NOT NULL, " +
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
            ContentValues values = AnimalsDaoHelper.createValuesFromAnimal(animal);
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
                animals.add(AnimalsDaoHelper.createAnimal(cursor));
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
            cursor = db.query(TABLE_NAME, null, AnimalsContract.Animal._ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor.moveToFirst()) {
                animal = AnimalsDaoHelper.createAnimal(cursor);
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
            rowUpdated = db.update(TABLE_NAME, AnimalsDaoHelper.createValuesFromAnimal(animal),
                    AnimalsContract.Animal._ID + " = ?", new String[]{String.valueOf(animal.getId())});
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
            isAnimalDeleted = db.delete(TABLE_NAME, AnimalsContract.Animal._ID + " = ?", new String[]{String.valueOf(animal.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return isAnimalDeleted;
    }

}