package bodya.sbt.ru.currentwork;


import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    private static final String ARG_ANIMAL = "animal";

    public static BaseFragment newInstance(Animal animal) {
        BaseFragment fragment = new AnimalInfoFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_ANIMAL, animal);
        fragment.setArguments(args);

        return fragment;
    }

    protected Animal getAnimalDetails() {
        return (Animal) getArguments().getSerializable(ARG_ANIMAL);
    }
}
