package ru.boyda.popov.currencyconverter.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.boyda.popov.currencyconverter.R;
import ru.boyda.popov.currencyconverter.networking.Currency;

public class ListAdapter extends BaseAdapter {

    private List<Currency> currencies;

    public ListAdapter(List<Currency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public Currency getItem(int position) {
        return currencies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.nominal = (TextView) view.findViewById(R.id.nominal);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.numcode = (TextView) view.findViewById(R.id.num_code);
            holder.value = (TextView) view.findViewById(R.id.value);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        Currency currency = getItem(position);
        holder.nominal.setText(String.valueOf(currency.getNominal()));
        holder.name.setText(String.valueOf(currency.getName()));
        holder.numcode.setText(String.valueOf(currency.getNumCode()));
        holder.value.setText(String.valueOf(currency.getValue()));

        return view;
    }

    private static class ViewHolder {
        private TextView nominal;
        private TextView name;
        private TextView numcode;
        private TextView value;
    }
}
