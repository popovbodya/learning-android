package ru.dimasokol.currencies;

import android.app.Application;
import android.support.annotation.NonNull;

import ru.dimasokol.currencies.storage.CurrenciesStorage;
import ru.dimasokol.currencies.storage.HistoryStorage;

/**
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */

public class CurrenciesApplication extends Application {

    private final CurrenciesStorage mCurrenciesStorage = new CurrenciesStorage();
    private final HistoryStorage mHistoryStorage = new HistoryStorage();

    @NonNull
    public CurrenciesStorage getCurrenciesStorage() {
        return mCurrenciesStorage;
    }

    @NonNull
    public HistoryStorage getHistoryStorage() {
        return mHistoryStorage;
    }
}
