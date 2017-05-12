package ru.boyda.popov.searchcinemas;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.boyda.popov.searchcinemas.parser.desc.CinemaDetails;

public class CinemasAdapter extends BaseAdapter {

    private List<CinemaDetails> cinemaDetailsList;
    private int size;

    public CinemasAdapter(List<CinemaDetails> cinemaDetailsList) {
        this.cinemaDetailsList = cinemaDetailsList;
        size = cinemaDetailsList.size();
    }

    @Override
    public int getCount() {
        return cinemaDetailsList.size();
    }

    @Override
    public CinemaDetails getItem(int position) {
        return cinemaDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        TextView view = (TextView) convertView;

        if (view == null) {
            view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        view.setText(getItem(position).getName());
        return view;
    }
}
