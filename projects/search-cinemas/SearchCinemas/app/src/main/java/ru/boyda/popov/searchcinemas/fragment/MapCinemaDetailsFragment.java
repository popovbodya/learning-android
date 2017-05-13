package ru.boyda.popov.searchcinemas.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.boyda.popov.searchcinemas.R;
import ru.boyda.popov.searchcinemas.parser.geo.Location;

public class MapCinemaDetailsFragment extends BaseCinemaDetailsFragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) root;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        Location cinemaLocation = getDetails().getGeometry().getLocation();
        LatLng point = new LatLng(Double.valueOf(cinemaLocation.getLat()), Double.valueOf(cinemaLocation.getLng()));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17f));
        MarkerOptions marker = new MarkerOptions();
        marker.position(point).title(getDetails().getName());
        map.addMarker(marker);
    }
}
