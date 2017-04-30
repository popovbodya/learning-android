package ru.dimasokol.learning.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */

public class LoggingAdapter extends BaseAdapter implements LoggingStorage.IMessagesListener {

    private final LoggingStorage mStorage;

    public LoggingAdapter(LoggingStorage storage) {
        mStorage = storage;
    }

    public void startListening() {
        mStorage.setListener(this);
    }

    public void stopListening() {
        mStorage.setListener(null);
    }

    @Override
    public int getCount() {
        return mStorage.size();
    }

    @Override
    public LoggingStorage.LogMessage getItem(int position) {
        return mStorage.getMessage(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getTimestamp();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        view.setText(getItem(position).getMessage());
        return view;
    }

    @Override
    public void onMessagesChanged(LoggingStorage storage) {
        notifyDataSetChanged();
    }
}
