package ru.boyda.popov.currencyconverter.storage;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.boyda.popov.currencyconverter.networking.CurrenciesList;

public class Loader extends AsyncTask<Void, Void, CurrenciesList> {

    private CurrenciesStorage storage;

    public Loader(CurrenciesStorage storage) {
        this.storage = storage;
    }

    @Override
    protected CurrenciesList doInBackground(Void... params) {
        InputStream is;
        CurrenciesList list = null;

        try {
            String urlString = "http://www.cbr.ru/scripts/XML_daily.asp";
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            is = urlConnection.getInputStream();
            list = CurrenciesList.readFromStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(CurrenciesList currenciesList) {
        if (storage.getListener().get() != null) {
            storage.setLoadedList(currenciesList);
            storage.getListener().get().onDataChanged(storage);
        }
    }
}
