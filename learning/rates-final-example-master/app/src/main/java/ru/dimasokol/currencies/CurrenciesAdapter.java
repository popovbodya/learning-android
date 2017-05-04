package ru.dimasokol.currencies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ru.dimasokol.currencies.networking.CurrenciesList;
import ru.dimasokol.currencies.networking.Currency;

/**
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */

public class CurrenciesAdapter extends BaseAdapter {

    private final CurrenciesList mCurrenciesList;
    private LayoutInflater mLayoutInflater;

    public CurrenciesAdapter(CurrenciesList currenciesList) {
        mCurrenciesList = currenciesList;
    }

    @Override
    public int getCount() {
        return mCurrenciesList.getCurrencies().size();
    }

    @Override
    public Currency getItem(int position) {
        return mCurrenciesList.getCurrencies().get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCurrenciesList.getCurrencies().get(position).getNumCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }

        View view = convertView;

        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.currency_list_item, parent, false);

            CurrencyHolder freshHolder = new CurrencyHolder();
            freshHolder.mCurrencyName = (TextView) view.findViewById(R.id.currency_name);
            freshHolder.mCurrencyCode = (TextView) view.findViewById(R.id.currency_code);
            freshHolder.mCurrencyRate = (TextView) view.findViewById(R.id.currency_rate);

            view.setTag(freshHolder);
        }

        CurrencyHolder holder = (CurrencyHolder) view.getTag();
        Currency currency = getItem(position);

        holder.mCurrencyName.setText(parent.getResources().getString(R.string.format_currency_name,
                currency.getNominal().intValue(), currency.getName()));
        holder.mCurrencyCode.setText(parent.getResources().getString(R.string.format_currency_codes,
                currency.getCharCode(), currency.getNumCode()));
        holder.mCurrencyRate.setText(parent.getResources().getString(R.string.format_currency_rate,
                currency.getValue()));

        return view;
    }

    private static class CurrencyHolder {
        TextView mCurrencyName;
        TextView mCurrencyCode;
        TextView mCurrencyRate;
    }
}
