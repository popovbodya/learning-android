package ru.boyda.popov.searchcinemas;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.List;

import ru.boyda.popov.searchcinemas.fragment.BaseCinemaDetailsFragment;
import ru.boyda.popov.searchcinemas.fragment.CinemasListFragment;
import ru.boyda.popov.searchcinemas.interfaces.CinemaDetailsHost;
import ru.boyda.popov.searchcinemas.parser.geo.CinemaDetails;

public class MainActivity extends FragmentActivity implements CinemaDetailsHost {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        if (savedInstanceState == null) {
            CinemasListFragment fragment = new CinemasListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_for_list, fragment)
                    .commit();
        }
    }


    @Override
    public void displayCinemaWithDetails(CinemaDetails cinemaDetails, CinemaDetailsType cinemaDetailsType) {
        BaseCinemaDetailsFragment fragment = BaseCinemaDetailsFragment.newInstance(cinemaDetails, cinemaDetailsType);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_for_map, fragment)
                .commit();
    }

}
