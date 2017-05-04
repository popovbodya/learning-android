package ru.dimasokol.currencies.storage;

import java.util.HashMap;
import java.util.Map;

import ru.dimasokol.currencies.networking.CurrencyHistory;

/**
 * Хранилище истории по валютам
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */
public final class HistoryStorage {

    private Map<String, CurrencyHistory> mHistories = new HashMap<>();

    public synchronized boolean isReady(String currencyCode) {
        return mHistories.containsKey(currencyCode);
    }

    public synchronized void addHistory(String currencyCode, CurrencyHistory history) {
        mHistories.put(currencyCode, history);
    }

    public synchronized CurrencyHistory getHistory(String currencyCode) {
        return mHistories.get(currencyCode);
    }

}
