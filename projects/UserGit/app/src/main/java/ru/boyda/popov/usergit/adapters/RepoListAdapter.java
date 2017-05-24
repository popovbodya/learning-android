package ru.boyda.popov.usergit.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.usergit.R;
import ru.boyda.popov.usergit.pojo.Repository;

public class RepoListAdapter extends BaseAdapter {

    private List<Repository> userList;

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Repository getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentView = convertView;

        if (currentView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            currentView = inflater.inflate(R.layout.repo_list_item, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.index = (TextView) currentView.findViewById(R.id.repo_index);
            holder.repositoryName = (TextView) currentView.findViewById(R.id.repo_name);
            holder.size = (TextView) currentView.findViewById(R.id.repo_size);
            currentView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) currentView.getTag();
        Context context = currentView.getContext();
        Repository repository = getItem(position);
        holder.index.setText(String.valueOf(position + 1));
        holder.repositoryName.setText(repository.getName());
        holder.size.setText(context.getString(R.string.repo_size, repository.getSize()));
        return currentView;
    }

    public void setUserList(List<Repository> repositoryList) {
        userList.clear();
        userList.addAll(repositoryList);
        notifyDataSetChanged();
    }

    public RepoListAdapter() {
        userList = new ArrayList<>();
    }

    private class ViewHolder {
        private TextView index;
        private TextView repositoryName;
        private TextView size;
    }

}