package bodya.sbt.ru.currentwork.db;


import android.provider.BaseColumns;

public class AnimalsContract {

    public static class Animal implements BaseColumns {

        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String TYPE = "type";
        public static final String WEIGHT = "weight";
        public static final String HEIGHT = "height";

    }
}