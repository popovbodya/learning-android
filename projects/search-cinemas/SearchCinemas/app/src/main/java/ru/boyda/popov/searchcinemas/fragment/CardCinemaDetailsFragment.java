package ru.boyda.popov.searchcinemas.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.boyda.popov.searchcinemas.R;

public class CardCinemaDetailsFragment extends BaseCinemaDetailsFragment {

    private TextView name;
    private TextView address;
    private TextView score;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.cinema_card_view, container, false);
        name = (TextView) root.findViewById(R.id.cinema_name_plus_icon);
        address = (TextView) root.findViewById(R.id.address_text_view);
        score = (TextView) root.findViewById(R.id.score_text_view);

        name.setText(getString(R.string.cinema_name, getDetails().getName()));
        address.setText(getString(R.string.cinema_address, getDetails().getFormattedAddress()));

        String rating = getDetails().getRating();
        if (rating != null) {
            score.setText(getString(R.string.cinema_rating, rating));
        } else {
            score.setText(getString(R.string.cinema_rating, "0"));
        }

        return root;
    }

}
