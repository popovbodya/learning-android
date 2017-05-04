package ru.dimasokol.currencies;

import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.dimasokol.currencies.networking.CurrenciesList;

/**
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */

public class RatesLoadTask extends AsyncTask<Void, Void, CurrenciesList> {

    private WeakReference<IRatesLoadingTarget> mTarget;
    private CurrenciesApplication mApplication;

    public RatesLoadTask(IRatesLoadingTarget target) {
        mTarget = new WeakReference<>(target);
        mApplication = target.getCurrenciesApplication();
    }

    @Override
    protected CurrenciesList doInBackground(Void... params) {

        try {
            URL serviceUrl = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
            HttpURLConnection connection = (HttpURLConnection) serviceUrl.openConnection();

            CurrenciesList list = CurrenciesList.readFromStream(connection.getInputStream());
            return list;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(CurrenciesList currenciesList) {
        IRatesLoadingTarget target = mTarget.get();

        if (target != null) {
            if (currenciesList != null) {
                mApplication.getCurrenciesStorage().setLoadedList(currenciesList);
                target.onSuccess();
            } else {
                target.onError();
            }
        }
    }

    public interface IRatesLoadingTarget {
        CurrenciesApplication getCurrenciesApplication();
        void onSuccess();
        void onError();
    }

}
