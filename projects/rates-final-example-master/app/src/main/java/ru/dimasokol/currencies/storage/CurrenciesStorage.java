package ru.dimasokol.currencies.storage;

import android.support.annotation.Nullable;

import ru.dimasokol.currencies.networking.CurrenciesList;
import ru.dimasokol.currencies.networking.Currency;

/**
 * @author Дмитрий Соколов <me@dimasokol.ru>
 */
public final class CurrenciesStorage {

    private CurrenciesList mLoadedList;

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
            for (Currency currency: mLoadedList.getCurrencies()) {
                if (currency.getCharCode().equals(code)) {
                    return currency;
                }
            }
        }

        return null;
    }
}
