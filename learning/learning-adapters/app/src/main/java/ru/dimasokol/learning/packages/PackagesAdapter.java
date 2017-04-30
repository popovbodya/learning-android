package ru.dimasokol.learning.packages;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public class PackagesAdapter extends BaseAdapter {

    private static final String TAG = "PackagesAdapter";
    private List<PackageInfo> mPackages;
    int count = 0;

    public PackagesAdapter(List<PackageInfo> packages) {
        this.mPackages = Collections.unmodifiableList(packages);
    }

    @Override
    public int getCount() {
        return mPackages.size();
    }

    @Override
    public PackageInfo getItem(int position) {
        return mPackages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).packageName.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {

            Log.i(TAG, "index " + ++count);
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.package_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.mIcon = (ImageView) view.findViewById(R.id.package_icon);
            holder.mTitle = (TextView) view.findViewById(R.id.package_title);
            holder.mSubtitle = (TextView) view.findViewById(R.id.package_subtitle);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        PackageInfo packageInfo = getItem(position);

        holder.mSubtitle.setText(packageInfo.packageName);

        if (packageInfo.applicationInfo != null) {
            holder.mTitle.setText(packageInfo.packageName);
            holder.mIcon.setImageResource(R.mipmap.ic_no_icon);

            InfoLoader loader = new InfoLoader(packageInfo.applicationInfo,
                    holder.mIcon, holder.mTitle);
            loader.execute();
        } else {
            holder.mTitle.setText(R.string.package_system);
            holder.mIcon.setImageResource(R.mipmap.ic_no_icon);
        }

        return view;
    }

    private static class ViewHolder {
        private TextView mTitle;
        private TextView mSubtitle;
        private ImageView mIcon;
    }

    private static class InfoLoader extends AsyncTask<Void, Void, InfoLoader.InfoBundle> {

        private static String TAG = "InfoLoader";

        private ApplicationInfo mPackage;
        private WeakReference<ImageView> mImageTarget;
        private WeakReference<TextView> mTextTarget;
        private Context mContext;

        public InfoLoader(ApplicationInfo packageName, ImageView imageTarget, TextView textTarget) {
            mPackage = packageName;
            mImageTarget = new WeakReference<>(imageTarget);
            mTextTarget = new WeakReference<>(textTarget);
            imageTarget.setTag(packageName.packageName);

            mContext = imageTarget.getContext().getApplicationContext();
        }

        @Override
        protected InfoBundle doInBackground(Void... params) {
            InfoBundle bundle = new InfoBundle();
            bundle.mIcon = mContext.getPackageManager().getApplicationIcon(mPackage);
            bundle.mTitle = mContext.getPackageManager().getApplicationLabel(mPackage);

            return bundle;
        }

        @Override
        protected void onPostExecute(InfoBundle bundle) {
            ImageView imageView = mImageTarget.get();
            TextView textView = mTextTarget.get();

            if (bundle == null || imageView == null || textView == null ||
                    !mPackage.packageName.equals(imageView.getTag().toString())) {
                if (bundle == null) {
                    Log.i(TAG, "bundle is null");
                }
                return;
            }

            imageView.setImageDrawable(bundle.mIcon);
            textView.setText(bundle.mTitle);
        }

        static class InfoBundle {
            Drawable mIcon;
            CharSequence mTitle;
        }
    }
}
