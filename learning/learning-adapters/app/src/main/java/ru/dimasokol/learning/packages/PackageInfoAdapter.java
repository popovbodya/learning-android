package ru.dimasokol.learning.packages;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PackageInfoAdapter extends BaseAdapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ACTIVITY = 1;
    private static final int TYPE_PERMISSION = 2;

    private static final int[] TITLES = new int[] {
            R.string.title_activities,
            R.string.title_permissions
    };

    private List<ActivityInfo> mActivities;
    private List<String> mPermissions;

    public PackageInfoAdapter(List<ActivityInfo> activities, List<String> permissions) {
        mActivities = activities;
        mPermissions = permissions;
    }

    @Override
    public int getCount() {
        return mActivities.size() + mPermissions.size() + TITLES.length;
    }

    @Override
    public Object getItem(int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                return (position == 0)? TITLES[0] : TITLES[1];
            case TYPE_ACTIVITY:
                return mActivities.get(position - 1);
            case TYPE_PERMISSION:
                return mPermissions.get(position - mActivities.size() - 2);
        }

        throw new IllegalStateException();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            if (getItemViewType(position) == TYPE_HEADER) {
                view = (TextView) inflater.inflate(R.layout.header_list_item, parent, false);
            } else {
                view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }
        }

        PackageManager pm = parent.getContext().getPackageManager();

        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                view.setText((Integer) getItem(position));
                break;
            case TYPE_ACTIVITY:
                ActivityInfo activityInfo = (ActivityInfo) getItem(position);
                CharSequence activityLabel = activityInfo.loadLabel(pm);
                view.setText(parent.getContext().getString(R.string.activity_info_format,
                        activityLabel, activityInfo));
                break;
            case TYPE_PERMISSION:
                String permission = getItem(position).toString();
                try {
                    PermissionInfo permInfo = pm.getPermissionInfo(permission,
                            PackageManager.GET_META_DATA);
                    CharSequence info = permInfo.loadDescription(pm);

                    if (!TextUtils.isEmpty(info)) {
                        view.setText(info);
                    } else {
                        view.setText(permission);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    view.setText(permission);
                }

                break;
        }

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mActivities.size() + 1) {
            return TYPE_HEADER;
        }

        if (position < mActivities.size() + 1) {
            return TYPE_ACTIVITY;
        }

        return TYPE_PERMISSION;
    }
}
