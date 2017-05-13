package ru.boyda.popov.searchcinemas.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.boyda.popov.searchcinemas.CinemaDetailsType;
import ru.boyda.popov.searchcinemas.parser.geo.CinemaDetails;

public class BaseCinemaDetailsFragment extends Fragment {

    private static final String ARG_CINEMA = "cinema";

    public static BaseCinemaDetailsFragment newInstance(CinemaDetails details, CinemaDetailsType type) {
        BaseCinemaDetailsFragment fragment;

        if (type == null || type == CinemaDetailsType.CardView) {
            fragment = new CardCinemaDetailsFragment();
        } else {
            fragment = new MapCinemaDetailsFragment();
        }

        Bundle args = new Bundle();
        args.putSerializable(ARG_CINEMA, details);
        fragment.setArguments(args);

        return fragment;
    }

    protected CinemaDetails getDetails() {
        return (CinemaDetails) getArguments().getSerializable(ARG_CINEMA);
    }
}
