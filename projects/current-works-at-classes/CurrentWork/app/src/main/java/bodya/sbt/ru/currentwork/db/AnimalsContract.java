package bodya.sbt.ru.currentwork.db;


import android.provider.BaseColumns;

class AnimalsContract {

    public static class Animal implements BaseColumns {

        static final String NAME = "name";
        static final String AGE = "age";
        static final String TYPE = "type";
        static final String WEIGHT = "weight";
        static final String HEIGHT = "height";

    }
}