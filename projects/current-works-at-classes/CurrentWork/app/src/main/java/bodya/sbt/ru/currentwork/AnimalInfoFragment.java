package bodya.sbt.ru.currentwork;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AnimalInfoFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.card_view, container, false);

        TextView nameTextView = (TextView) root.findViewById(R.id.name);
        TextView heightTextView = (TextView) root.findViewById(R.id.height);
        TextView weightTextView = (TextView) root.findViewById(R.id.weight);
        TextView ageTextView = (TextView) root.findViewById(R.id.age);
        TextView typeTextView = (TextView) root.findViewById(R.id.animal_type);

        nameTextView.setText(getString(R.string.name, getAnimalDetails().getName()));
        ageTextView.setText(getString(R.string.age, getAnimalDetails().getAge()));
        heightTextView.setText(getString(R.string.height, getAnimalDetails().getHeight()));
        weightTextView.setText(getString(R.string.weight, getAnimalDetails().getWeight()));
        typeTextView.setText(getString(R.string.type, getAnimalDetails().getAnimalType()));
        return root;
    }
}
