package ru.dimasokol.currencies;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.dimasokol.currencies.diagram.DiagramDrawable;
import ru.dimasokol.currencies.networking.Currency;
import ru.dimasokol.currencies.networking.CurrencyHistory;
import ru.dimasokol.currencies.networking.HistoryRecord;

public class CalculatorActivity extends Activity implements HistoryLoadTask.IHistoryLoadingTarget {

    public static final String EXTRA_CODE = "CalculatorActivity.EXTRA_CODE";

    private EditText mAmountEdit;
    private TextView mCurrencyCodeView;
    private TextView mResultView;
    private View mDiagramView;
    private View mProgressView;
    private View mNoHistoryView;

    private Currency mCurrency;
    private HistoryLoadTask mHistoryLoadTask;
    private ValueAnimator mDiagramAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        mDiagramView = findViewById(R.id.diagram);
        mProgressView = findViewById(R.id.progress_loading);
        mNoHistoryView = findViewById(R.id.no_history);

        String code = getIntent().getStringExtra(EXTRA_CODE);
        mCurrency = ((CurrenciesApplication) getApplication()).getCurrenciesStorage()
                .findByCode(code);

        if (mCurrency == null) {
            finish();
            return;
        }

        mAmountEdit = (EditText) findViewById(R.id.amount);
        mCurrencyCodeView = (TextView) findViewById(R.id.currency_code);
        mResultView = (TextView) findViewById(R.id.result);

        mAmountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                performCalculation();
            }
        });

        mCurrencyCodeView.setText(mCurrency.getCharCode());

        if (savedInstanceState == null) {
            mAmountEdit.setText(Integer.toString(mCurrency.getNominal().intValue()));
            performCalculation();
        }

        showProgress(false);
        showNoHistory(false);
        if (!displayHistory()) {
            tryToLoadHistory();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDiagramAnimator != null) {
            mDiagramAnimator.removeAllUpdateListeners();
            mDiagramAnimator = null;
        }

        if (mHistoryLoadTask != null) {
            mHistoryLoadTask.cancel(false);
            mHistoryLoadTask = null;
        }
    }

    private void performCalculation() {
        String amount = mAmountEdit.getText().toString().trim();

        if (TextUtils.isEmpty(amount)) {
            mResultView.setText("");
            mAmountEdit.setError(null);
            return;
        }

        double value;
        try {
            value = Double.parseDouble(mAmountEdit.getText().toString());
        } catch (NumberFormatException e) {
            mAmountEdit.setError(getString(R.string.error_bad_amount));
            return;
        }

        double result = (value * mCurrency.getValue()) / mCurrency.getNominal();
        mResultView.setText(getString(R.string.format_conversion_result, result));
        mAmountEdit.setError(null);
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show? View.VISIBLE : View.GONE);
    }

    private void showNoHistory(boolean show) {
        mNoHistoryView.setVisibility(show? View.VISIBLE : View.GONE);
    }

    private void tryToLoadHistory() {
        showProgress(true);
        mHistoryLoadTask = new HistoryLoadTask(this, mCurrency.getId());
        mHistoryLoadTask.execute();
    }

    private boolean displayHistory() {
        CurrencyHistory history = getCurrenciesApplication().getHistoryStorage()
                .getHistory(mCurrency.getId());

        if (history != null) {
            if (history.getHistory().size() > 1) {
                List<DiagramDrawable.Point> points = new ArrayList<>(history.getHistory().size());

                for (HistoryRecord record : history.getHistory()) {
                    points.add(new DiagramDrawable.Point(getString(R.string.format_currency_rate,
                            record.getValue()), record.getValue()));
                }

                DiagramDrawable drawable = new DiagramDrawable(points);
                drawable.setLineColor(getResources().getColor(R.color.colorAccent));
                mDiagramView.setBackgroundDrawable(drawable);

                mDiagramAnimator = ValueAnimator.ofInt(0, 10000);
                mDiagramAnimator.setDuration(1200);
                mDiagramAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                mDiagramAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mDiagramView.getBackground().setLevel((Integer) animation.getAnimatedValue());
                    }
                });
                mDiagramAnimator.start();
            } else {
                showNoHistory(true);
            }

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
        displayHistory();
        mHistoryLoadTask = null;

    }

    @Override
    public void onError() {
        showProgress(false);
        mHistoryLoadTask = null;
    }
}
