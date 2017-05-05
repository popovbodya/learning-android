package ru.boyda.popov.recyclerviewfirsttry;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PhotoHolder> {

    private ArrayList<Photo> mPhotos;

    public RecyclerAdapter(ArrayList<Photo> mPhotos) {
        this.mPhotos = mPhotos;
    }

    @Override
    public RecyclerAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.PhotoHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
