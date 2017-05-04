package ru.dimasokol.currencies;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.dimasokol.currencies.networking.CurrenciesList;
import ru.dimasokol.currencies.networking.Currency;

public class RatesActivity extends Activity implements RatesLoadTask.IRatesLoadingTarget {

    private ListView mRatesList;
    private View mErrorLayout;
    private View mRetryButton;
    private View mProgressView;

    private CurrenciesAdapter mAdapter;
    private RatesLoadTask mRunningTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            onPortraitMode();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!displayRatesLandMode()) {
                tryToLoadRates();
            }
        }
    }


    private void onPortraitMode() {

        for (int i = 0; i < 2; i++) {
            Fragment fragment = getFragmentManager().findFragmentByTag("TAG" + i);
            if (fragment != null) {
                Log.i("TAG" + i, "YES");
                getFragmentManager()
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }


        mRatesList = (ListView) findViewById(R.id.currencies_list);
        mErrorLayout = findViewById(R.id.layout_error);
        mRetryButton = findViewById(R.id.button_try_again);
        mProgressView = findViewById(R.id.progress_loading);

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showError(false);
                showProgress(true);
                tryToLoadRates();
            }
        });

        mRatesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Currency currency = mAdapter.getItem(position);
                Intent calculator = new Intent(RatesActivity.this, CalculatorActivity.class);
                calculator.putExtra(CalculatorActivity.EXTRA_CODE, currency.getCharCode());
                startActivity(calculator);
            }
        });

        showError(false);

        if (!displayRatesPortMode()) {
            showProgress(true);
            tryToLoadRates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mRunningTask != null) {
            mRunningTask.cancel(false);
        }
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(boolean show) {
        mErrorLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void tryToLoadRates() {
        if (mRunningTask != null && !mRunningTask.isCancelled()) {
            return;
        }

        mRunningTask = new RatesLoadTask(this);
        mRunningTask.execute();
    }

    private boolean displayRatesLandMode() {
        if (getCurrenciesApplication().getCurrenciesStorage().isReady()) {

            CurrenciesList loadedList = getCurrenciesApplication().getCurrenciesStorage().getLoadedList();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            for (int i = 0; i < 2; i++) {
                FragmentList fragmentList = FragmentList.newInstance(loadedList);
                transaction.add(R.id.activity_rates, fragmentList, "TAG" + i);
            }
            transaction.commit();
            return true;
        }
        return false;
    }

    private boolean displayRatesPortMode() {
        if (getCurrenciesApplication().getCurrenciesStorage().isReady()) {

            CurrenciesList loadedList = getCurrenciesApplication().getCurrenciesStorage().getLoadedList();

            mAdapter = new CurrenciesAdapter(loadedList);
            mRatesList.setAdapter(mAdapter);
            showProgress(false);
            showError(false);


            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            for (int i = 0; i < 2; i++) {
                FragmentList fragmentList = FragmentList.newInstance(loadedList);
                transaction.add(R.id.activity_rates, fragmentList);
            }
            transaction.commit();
            return true;
        }
        return false;
    }

    @Override
    public CurrenciesApplication getCurrenciesApplication() {
        return (CurrenciesApplication) getApplication();
    }

    @Override
    public void onSuccess() {
        showProgress(false);
        showError(false);
        displayRatesPortMode();
        mRunningTask = null;
    }

    @Override
    public void onError() {
        showProgress(false);
        showError(true);
        mRunningTask = null;
    }
}
