package ru.boyda.popov.currencyconverter;

import android.app.Application;

import ru.boyda.popov.currencyconverter.storage.CurrenciesStorage;


public class MyApplication extends Application {

    private CurrenciesStorage mStorage = new CurrenciesStorage();

    public CurrenciesStorage getStorage() {
        return mStorage;
    }

}
