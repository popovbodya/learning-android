package ru.boyda.popov.searchcinemas;


import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.searchcinemas.interfaces.CinemaDetailsListener;
import ru.boyda.popov.searchcinemas.parser.geo.CinemaDetails;
import ru.boyda.popov.searchcinemas.parser.geo.GeoResponse;

public class LoadTask extends AsyncTask<Void, Void, List<CinemaDetails>> {

    private WeakReference<CinemaDetailsListener> listenerRef;
    private final String API_KEY = "AIzaSyCeGZSUUx4PjkA-_jC3CPaT1LMYpeq66L4";

    public LoadTask(CinemaDetailsListener listener) {
        listenerRef = new WeakReference<>(listener);
    }

    @Override
    protected List<CinemaDetails> doInBackground(Void... params) {
        List<CinemaDetails> cinemaDetailsList = new ArrayList<>();
        try {
            GeoResponse geoResponse = getGeoResponse();
            cinemaDetailsList.addAll(geoResponse.getResults());

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
        URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?pagetoken&language=ru&query=кинотеатры%20в%20москве&key=" + API_KEY);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(url, GeoResponse.class);
    }


}
