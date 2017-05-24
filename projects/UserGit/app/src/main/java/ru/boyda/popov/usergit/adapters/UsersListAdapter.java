package ru.boyda.popov.usergit.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.usergit.R;
import ru.boyda.popov.usergit.pojo.User;

public class UsersListAdapter extends BaseAdapter {

    private List<User> userList;

    private class ViewHolder {
        private TextView login;
        private TextView url;
    }

    public UsersListAdapter() {
        this.userList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
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
            view = inflater.inflate(R.layout.item_list, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.login = (TextView) view.findViewById(R.id.login);
            holder.url = (TextView) view.findViewById(R.id.url);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        User user = getItem(position);
        holder.login.setText(user.getUsername());
        holder.url.setText(user.getUrl());

        return view;
    }

    public void setUserList(List<User> animals) {
        userList.clear();
        userList.addAll(animals);
        notifyDataSetChanged();
    }

}