package ru.boyda.popov.searchcinemas;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.searchcinemas.parser.geo.CinemaDetails;
import ru.boyda.popov.searchcinemas.parser.geo.GeoResponse;

public class Loader extends HandlerThread {

    private static final String TAG = Loader.class.getSimpleName();
    private static final String API_KEY = "AIzaSyCeGZSUUx4PjkA-_jC3CPaT1LMYpeq66L4";
    private static final int DOWNLOAD_NEW_CINEMAS_TASK = 0;

    private Handler mWorkerHandler;
    private Handler mResponseHandler;
    private Callback mCallback;
    private String nextPageToken;
    private int counter;

    public interface Callback {
        void onCinemasDownloaded(List<CinemaDetails> cinemaDetailsList);
    }

    public Loader(Handler responseHandler, Callback callback) {
        super(TAG);
        mResponseHandler = responseHandler;
        mCallback = callback;
    }

    public void queueTask() {
        Log.e(TAG, "task with the number: " + counter + " added to the queue");
        mWorkerHandler.obtainMessage(DOWNLOAD_NEW_CINEMAS_TASK).sendToTarget();
    }

    public void prepareHandler() {
        mWorkerHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                final List<CinemaDetails> cinemaDetailsList = new ArrayList<>();
                try {
                    GeoResponse geoResponse = getGeoResponse();

                    if (geoResponse != null) {
                        cinemaDetailsList.addAll(geoResponse.getResults());
                        nextPageToken = geoResponse.getNextPageToken();
                        counter++;
                        mResponseHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onCinemasDownloaded(cinemaDetailsList);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }
        });
    }


    private GeoResponse getGeoResponse() throws IOException {
        URL url;
        if (nextPageToken != null) {
            url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?pagetoken=" + nextPageToken + "&language=ru&query=кинотеатры%20в%20москве&key=" + API_KEY);
        } else {
            url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?pagetoken&language=ru&query=кинотеатры%20в%20москве&key=" + API_KEY);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(url, GeoResponse.class);
    }

}
