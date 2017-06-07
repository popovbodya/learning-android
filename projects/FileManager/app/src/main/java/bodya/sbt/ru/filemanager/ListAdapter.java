package bodya.sbt.ru.filemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private static final short KB = 1024;

    private List<File> directoriesList;

    private class ViewHolder {
        private ImageView iconImageView;
        private TextView dirTextView;
        private TextView additionalInfoTextView;
    }

    ListAdapter() {
        this.directoriesList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return directoriesList.size();
    }

    @Override
    public File getItem(int position) {
        return directoriesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        File file = getItem(position);
        return !file.isFile();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = createViewWithTag(parent);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        File file = getItem(position);
        if (file.isFile()) {
            configureFileElement(view, holder, file);
        } else {
            configureDirElement(view, holder, file);
        }
        holder.dirTextView.setText(file.getName());

        return view;
    }

    void setDirList(File[] dirList) {
        directoriesList.clear();
        if (dirList != null) {
            directoriesList.addAll(Arrays.asList(dirList));
        }
        notifyDataSetChanged();
    }

    private void configureDirElement(View view, ViewHolder holder, File file) {
        int count = getFileListSize(file);
        holder.additionalInfoTextView.setText(view.getResources().getString(R.string.folder_size, count));
        holder.iconImageView.setImageResource(R.drawable.folder_icon);
    }

    private void configureFileElement(View view, ViewHolder holder, File file) {
        long size = file.length() / KB;
        holder.additionalInfoTextView.setText(view.getResources().getString(R.string.file_size, size));
        holder.iconImageView.setImageResource(R.drawable.file_icon);
    }

    private View createViewWithTag(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.dirTextView = (TextView) view.findViewById(R.id.elem_dir);
        holder.iconImageView = (ImageView) view.findViewById(R.id.list_elem_icon);
        holder.additionalInfoTextView = (TextView) view.findViewById(R.id.additional_info);
        view.setTag(holder);
        return view;
    }

    private int getFileListSize(File file) {
        File[] listFiles = file.listFiles();
        return listFiles != null ? listFiles.length : 0;
    }


}
