package bodya.sbt.ru.currentwork.db;


import android.content.ContentValues;
import android.database.Cursor;

import bodya.sbt.ru.currentwork.Animal;

class AnimalsDaoHelper {

    private static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    private static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    static Animal createAnimal(Cursor cursor) {
        Animal animal = new Animal();
        animal.setId(AnimalsDaoHelper.getLong(cursor, AnimalsContract.Animal._ID));
        animal.setName(AnimalsDaoHelper.getString(cursor, AnimalsContract.Animal.NAME));
        animal.setAge(AnimalsDaoHelper.getInt(cursor, AnimalsContract.Animal.AGE));
        animal.setAnimalType(Animal.AnimalType.valueOf(AnimalsDaoHelper.getString(cursor, AnimalsContract.Animal.TYPE)));
        animal.setWeight(AnimalsDaoHelper.getInt(cursor, AnimalsContract.Animal.WEIGHT));
        animal.setHeight(AnimalsDaoHelper.getInt(cursor, AnimalsContract.Animal.HEIGHT));
        return animal;
    }

    static ContentValues createValuesFromAnimal(Animal animal) {
        ContentValues values = new ContentValues();
        values.put(AnimalsContract.Animal.NAME, animal.getName());
        values.put(AnimalsContract.Animal.AGE, animal.getAge());
        values.put(AnimalsContract.Animal.TYPE, animal.getAnimalType().toString());
        values.put(AnimalsContract.Animal.WEIGHT, animal.getWeight());
        values.put(AnimalsContract.Animal.HEIGHT, animal.getHeight());
        return values;
    }
}
