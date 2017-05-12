package ru.boyda.popov.searchcinemas;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.boyda.popov.searchcinemas.fragment.BaseCinemaDetailsFragment;
import ru.boyda.popov.searchcinemas.fragment.CinemasListFragment;
import ru.boyda.popov.searchcinemas.interfaces.CinemaDetailsHost;
import ru.boyda.popov.searchcinemas.parser.desc.CinemaDetails;

public class MapsActivity extends FragmentActivity implements CinemaDetailsHost {


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
    public void displayCinemaWithDetails(CinemaDetails cinemaDetails) {
        BaseCinemaDetailsFragment fragment = BaseCinemaDetailsFragment.newInstance(cinemaDetails);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_for_map, fragment)
                .commit();
    }
}
