package bodya.sbt.ru.currentwork;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnimalsAdapter extends BaseAdapter {

    private final List<Animal> animalList;

    public AnimalsAdapter() {
        animalList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return animalList.size();
    }

    @Override
    public Animal getItem(int position) {
        return animalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentView = convertView;
        if (currentView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            currentView = inflater.inflate(R.layout.animal_list_item, parent, false);
            currentView.setTag(new ViewHolder(currentView));
        }

        ViewHolder holder = (ViewHolder) currentView.getTag();
        Animal animal = animalList.get(position);

        Context context = currentView.getContext();
        holder.ageTextView.setText(context.getString(R.string.age, animal.getAge()));
        holder.nameTextView.setText(context.getString(R.string.name, animal.getName()));
        holder.animalTypeTextView.setText(context.getString(R.string.type, animal.getAnimalType()));
        holder.weightTextView.setText(context.getString(R.string.weight, animal.getWeight()));
        holder.heightTextView.setText(context.getString(R.string.height, animal.getHeight()));

        return currentView;
    }

    public void setAnimals(List<Animal> animals) {
        Log.e("adapter", "setAnimals");
        animalList.clear();
        if (animals != null) {
            animalList.addAll(animals);
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {

        final TextView ageTextView;
        final TextView nameTextView;
        final TextView weightTextView;
        final TextView heightTextView;
        final TextView animalTypeTextView;

        ViewHolder(View itemView) {
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            ageTextView = (TextView) itemView.findViewById(R.id.age_text_view);
            animalTypeTextView = (TextView) itemView.findViewById(R.id.animal_type_text_view);
            weightTextView = (TextView) itemView.findViewById(R.id.weight_text_view);
            heightTextView = (TextView) itemView.findViewById(R.id.height_text_view);
        }
    }
}

