package ru.popov.bodya.gitwatcher;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ListAdapter extends BaseAdapter implements UserStorage.OnContentChangedListener {

    private List<String> userList;


    private class ViewHolder {
        private TextView userName;
    }

    public ListAdapter() {
        this.userList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public String getItem(int position) {
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
            holder.userName = (TextView) view.findViewById(R.id.elem_username);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        String userName = getItem(position);
        holder.userName.setText(userName);

        return view;
    }

    @Override
    public void onContentChanged(Set<String> usernameSet) {
        setUserList(usernameSet);
    }

    void setUserList(Collection<String> usernameSet) {
        userList.clear();
        userList.addAll(usernameSet);
        notifyDataSetChanged();
    }
}
