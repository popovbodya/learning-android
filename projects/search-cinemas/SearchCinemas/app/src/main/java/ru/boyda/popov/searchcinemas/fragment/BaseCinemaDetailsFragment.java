package ru.boyda.popov.searchcinemas.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.boyda.popov.searchcinemas.parser.desc.CinemaDetails;

public class BaseCinemaDetailsFragment extends Fragment {

    private static final String ARG_CINEMA = "cinema";

    public static BaseCinemaDetailsFragment newInstance(CinemaDetails details) {
        BaseCinemaDetailsFragment fragment = new MapCinemaDetailsFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_CINEMA, details);
        fragment.setArguments(args);

        return fragment;
    }

    protected CinemaDetails getDetails() {
        return (CinemaDetails) getArguments().getSerializable(ARG_CINEMA);
    }
}
