package ru.boyda.popov.currencyconverter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ru.boyda.popov.currencyconverter.MyApplication;
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
            holder.numCode = (TextView) view.findViewById(R.id.num_code);
            holder.value = (TextView) view.findViewById(R.id.value);
            holder.charCode = (TextView) view.findViewById(R.id.char_code);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        Currency currency = getItem(position);
        holder.nominal.setText(String.valueOf((int) currency.getNominal().doubleValue()));
        holder.name.setText(String.valueOf(currency.getName()));

        String numCode = String.format("(%s)", String.valueOf(currency.getNumCode()));
        holder.numCode.setText(numCode);


        String value = String.format(Locale.ENGLISH, "%.2f", currency.getValue());
        holder.value.setText(String.valueOf(value));

        holder.charCode.setText(String.valueOf(currency.getCharCode()));

        return view;
    }

    private static class ViewHolder {
        private TextView nominal;
        private TextView name;
        private TextView numCode;
        private TextView value;
        private TextView charCode;

    }
}
