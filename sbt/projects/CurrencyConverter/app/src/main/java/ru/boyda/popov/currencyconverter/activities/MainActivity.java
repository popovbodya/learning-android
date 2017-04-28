package ru.boyda.popov.currencyconverter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import ru.boyda.popov.currencyconverter.MyApplication;
import ru.boyda.popov.currencyconverter.R;
import ru.boyda.popov.currencyconverter.adapters.ListAdapter;
import ru.boyda.popov.currencyconverter.storage.CurrenciesStorage;
import ru.boyda.popov.currencyconverter.storage.Loader;

public class MainActivity extends Activity implements CurrenciesStorage.IMessagesListener {

    private Loader mLoader;
    private CurrenciesStorage mStorage;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_main);

        mListView = (ListView) findViewById(R.id.messages_list);

        mStorage = ((MyApplication) getApplication()).getStorage();

        if (!mStorage.isReady()) {
            // заглушка
            startListening();
            mLoader = new Loader(mStorage);
            mLoader.execute();
        } else {
            onDataChanged(mStorage);
        }
    }

    @Override
    public void onDataChanged(CurrenciesStorage storage) {
        mListView.setAdapter(new ListAdapter(storage.getLoadedList().getCurrencies()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
        mLoader = null;
    }

    public void startListening() {
        mStorage.setListener(this);
    }

    public void stopListening() {
        mStorage.setListener(null);
    }
}
