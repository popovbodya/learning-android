package ru.dimasokol.currencies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

        if (!displayRates()) {
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
        mProgressView.setVisibility(show? View.VISIBLE : View.GONE);
    }

    private void showError(boolean show) {
        mErrorLayout.setVisibility(show? View.VISIBLE : View.GONE);
    }

    private void tryToLoadRates() {
        if (mRunningTask != null && !mRunningTask.isCancelled()) {
            return;
        }

        mRunningTask = new RatesLoadTask(this);
        mRunningTask.execute();
    }

    private boolean displayRates() {
        if (getCurrenciesApplication().getCurrenciesStorage().isReady()) {
            mAdapter = new CurrenciesAdapter(getCurrenciesApplication().getCurrenciesStorage()
                    .getLoadedList());
            mRatesList.setAdapter(mAdapter);
            showProgress(false);
            showError(false);

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
        displayRates();
        mRunningTask = null;
    }

    @Override
    public void onError() {
        showProgress(false);
        showError(true);
        mRunningTask = null;
    }
}
