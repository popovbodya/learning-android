package ru.dimasokol.currencies;

import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import ru.dimasokol.currencies.networking.CurrencyHistory;
import ru.dimasokol.currencies.utils.DateFormatUtils;

/**
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */

public class HistoryLoadTask extends AsyncTask<Void, Void, CurrencyHistory> {

    private static final int DAYS_IN_PAST = -5;

    private WeakReference<IHistoryLoadingTarget> mTargetRef;
    private String mCurrencyId;

    public HistoryLoadTask(IHistoryLoadingTarget target, String currencyId) {
        mTargetRef = new WeakReference<>(target);
        mCurrencyId = currencyId;
    }

    @Override
    protected CurrencyHistory doInBackground(Void... params) {
        try {
            Calendar now = Calendar.getInstance();
            Calendar past = Calendar.getInstance();
            past.add(Calendar.DAY_OF_MONTH, DAYS_IN_PAST);

            StringBuilder urlBuilder = new StringBuilder("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=");
            urlBuilder.append(DateFormatUtils.dateForRequest(past.getTime()))
                    .append("&date_req2=")
                    .append(DateFormatUtils.dateForRequest(now.getTime()))
                    .append("&VAL_NM_RQ=").append(mCurrencyId);

            URL serviceUrl = new URL(urlBuilder.toString());
            HttpURLConnection connection = (HttpURLConnection) serviceUrl.openConnection();

            CurrencyHistory list = CurrencyHistory.readFromStream(connection.getInputStream());
            return list;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(CurrencyHistory history) {
        IHistoryLoadingTarget target = mTargetRef.get();

        if (target != null) {
            if (history != null) {
                target.getCurrenciesApplication().getHistoryStorage().addHistory(mCurrencyId, history);
                target.onSuccess();
            } else {
                target.onError();
            }
        }
    }

    public interface IHistoryLoadingTarget {
        CurrenciesApplication getCurrenciesApplication();
        void onSuccess();
        void onError();
    }
}
