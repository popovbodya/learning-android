package bodya.sbt.ru.currentwork;


import android.app.Application;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private Worker worker;
    private static List<Animal> animalList;

    public MyApplication() {
        animalList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int age = (int) (Math.random() * 15) + 1;
            int height = (int) (Math.random() * 50) + 1;
            int weight = (int) (Math.random() * 50) + 1;
            animalList.add(new Animal("Animal " + i, age, height, weight));
        }
    }

    public List<Animal> getAnimalList() {
        return animalList;
    }

    public Worker getWorker(Handler handler, Worker.Callback callback) {
        if (worker == null) {
            worker = new Worker(handler, callback);
            worker.start();
            worker.prepareHandler();
        }
        return worker;
    }


}
