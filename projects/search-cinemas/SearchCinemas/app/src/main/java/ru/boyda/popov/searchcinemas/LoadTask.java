package ru.boyda.popov.searchcinemas;


import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.searchcinemas.interfaces.CinemaDetailsListener;
import ru.boyda.popov.searchcinemas.parser.desc.CinemaDetails;
import ru.boyda.popov.searchcinemas.parser.desc.DeskResponse;
import ru.boyda.popov.searchcinemas.parser.geo.GeoResponse;
import ru.boyda.popov.searchcinemas.parser.geo.Result;

public class LoadTask extends AsyncTask<Void, Void, List<CinemaDetails>> {

    private WeakReference<CinemaDetailsListener> listenerRef;
    private final String API_KEY = "AIzaSyAGti4f1uIhzPQMGwHXYs1mmsGxjBzIVpQ";

    public LoadTask(CinemaDetailsListener listener) {
        listenerRef = new WeakReference<>(listener);
    }

    @Override
    protected List<CinemaDetails> doInBackground(Void... params) {
        List<CinemaDetails> cinemaDetailsList = new ArrayList<>();
        try {
            GeoResponse geoResponse = getGeoResponse();
            for (Result result : geoResponse.getResults()) {
                cinemaDetailsList.add(getCinemaDetails(result.getPlaceId()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return cinemaDetailsList;
    }

    @Override
    protected void onPostExecute(List<CinemaDetails> cinemaDetails) {
        CinemaDetailsListener listener = listenerRef.get();
        if (listener != null) {
            listener.onReady(cinemaDetails);
        }
    }

    private GeoResponse getGeoResponse() throws IOException {
        URL url = new URL("https://maps.googleapis.com/maps/api/place/radarsearch/json?location=55.7475048,37.6203333&radius=5000&keyword=кинотеатр&language=ru&key=AIzaSyBg0KyLyomd-XTGpOX9TzNNUnYabywSx3w");
        ObjectMapper mapper = new ObjectMapper();
        GeoResponse geoResponse = mapper.readValue(url, GeoResponse.class);
        return geoResponse;
    }

    private CinemaDetails getCinemaDetails(String placeId) throws IOException {
        URL url = new URL("https://maps.googleapis.com/maps/api/place/details/json?language=ru&placeid=" + placeId + "&key=" + API_KEY);
        ObjectMapper mapper = new ObjectMapper();
        CinemaDetails result = mapper.readValue(url, DeskResponse.class).getResult();
        return result;
    }
}
