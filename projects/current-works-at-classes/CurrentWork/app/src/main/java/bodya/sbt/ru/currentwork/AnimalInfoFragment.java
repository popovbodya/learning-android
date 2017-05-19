package bodya.sbt.ru.currentwork;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AnimalInfoFragment extends Fragment {

    private static final String ARG_ANIMAL = "animal";
    private static final String TAG = "AnimalInfoFragment";
    private Animal args;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        args = (Animal) getArguments().getSerializable(ARG_ANIMAL);
        Log.e(TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    public static AnimalInfoFragment newInstance(Animal animal) {
        AnimalInfoFragment fragment = new AnimalInfoFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_ANIMAL, animal);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.card_view, container, false);

        TextView nameTextView = (TextView) root.findViewById(R.id.name);
        TextView heightTextView = (TextView) root.findViewById(R.id.height);
        TextView weightTextView = (TextView) root.findViewById(R.id.weight);
        TextView ageTextView = (TextView) root.findViewById(R.id.age);
        TextView typeTextView = (TextView) root.findViewById(R.id.animal_type);

        nameTextView.setText(getString(R.string.name, args.getName()));
        ageTextView.setText(getString(R.string.age, args.getAge()));
        heightTextView.setText(getString(R.string.height, args.getHeight()));
        weightTextView.setText(getString(R.string.weight, args.getWeight()));
        typeTextView.setText(getString(R.string.type, args.getAnimalType()));

        Log.e(TAG, "onCreateView");
        return root;
    }
}
