package bodya.sbt.ru.currentwork;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnimalsAdapter extends BaseAdapter {

    private final List<Animal> mAnimals;

    public AnimalsAdapter() {
        mAnimals = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mAnimals.size();
    }

    @Override
    public Animal getItem(int position) {
        return mAnimals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.animal_list_item, parent, false);
            itemView.setTag(new ViewHolder(itemView));
        }

        ViewHolder holder = (ViewHolder) itemView.getTag();

        Animal animal = mAnimals.get(position);
        Context context = itemView.getContext();

        holder.ageTextView.setText(context.getString(R.string.age, animal.getAge()));
        holder.nameTextView.setText(context.getString(R.string.name, animal.getName()));
        holder.weightTextView.setText(context.getString(R.string.weight, animal.getWeight()));
        holder.heightTextView.setText(context.getString(R.string.height, animal.getHeight()));

        return itemView;
    }

    public void setAnimals(List<Animal> animals) {
        mAnimals.clear();
        mAnimals.addAll(animals);
        notifyDataSetChanged();
    }

    private static class ViewHolder {

        final TextView ageTextView;
        final TextView nameTextView;
        final TextView weightTextView;
        final TextView heightTextView;

        ViewHolder(View itemView) {
            ageTextView = (TextView) itemView.findViewById(R.id.age_text_view);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            weightTextView = (TextView) itemView.findViewById(R.id.weight_text_view);
            heightTextView = (TextView) itemView.findViewById(R.id.height_text_view);
        }
    }
}

