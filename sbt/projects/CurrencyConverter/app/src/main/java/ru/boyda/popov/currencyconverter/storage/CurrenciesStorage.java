package ru.boyda.popov.currencyconverter.storage;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import ru.boyda.popov.currencyconverter.networking.CurrenciesList;
import ru.boyda.popov.currencyconverter.networking.Currency;

/**
 * Хранилище загруженного списка валют.
 */
public final class CurrenciesStorage {

    private CurrenciesList mLoadedList;
    private WeakReference<IMessagesListener> mListener = new WeakReference<>(null);

    public WeakReference<IMessagesListener> getListener() {
        return mListener;
    }

    public synchronized boolean isReady() {
        return mLoadedList != null;
    }

    public synchronized CurrenciesList getLoadedList() {
        return mLoadedList;
    }

    public synchronized void setLoadedList(CurrenciesList loadedList) {
        mLoadedList = loadedList;
    }

    @Nullable
    public synchronized Currency findByCode(@Nullable String code) {
        if (mLoadedList != null && code != null) {
            for (Currency currency : mLoadedList.getCurrencies()) {
                if (currency.getCharCode().equals(code)) {
                    return currency;
                }
            }
        }
        return null;
    }

    public void setListener(IMessagesListener listener) {
        mListener = new WeakReference<>(listener);
    }

    public interface IMessagesListener {
        void onDataChanged(CurrenciesStorage storage);
    }
}
