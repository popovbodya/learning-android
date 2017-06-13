package ru.popov.bodya.eventsmanager;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Event> eventList;

    public RecyclerViewAdapter() {
        eventList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_elem_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Event event = eventList.get(i);
        Context context = viewHolder.title.getContext();

        String eventTitle = event.getTitle();
        if (TextUtils.isEmpty(eventTitle)) {
            viewHolder.title.setText(context.getResources().getString(R.string.empty_event_title));
        } else {
            viewHolder.title.setText(event.getTitle());
        }

        String description = event.getDescription();
        if (TextUtils.isEmpty(description)) {
            viewHolder.description.setText(R.string.empty_event_desc);
        } else {
            viewHolder.description.setText(event.getDescription());
        }

        String startDateFromMills = DateHelper.getDateInFormat(Long.valueOf(event.getDateStart()));
        viewHolder.dtstart.setText(context.getResources().getString(R.string.event_start, startDateFromMills));

        String endDateFromMills = DateHelper.getDateInFormat(Long.valueOf(event.getDateEnd()));
        viewHolder.dtend.setText(context.getResources().getString(R.string.event_end, endDateFromMills));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView dtstart;
        private TextView dtend;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            description = (TextView) itemView.findViewById(R.id.description_text_view);
            dtstart = (TextView) itemView.findViewById(R.id.dtstart_text_view);
            dtend = (TextView) itemView.findViewById(R.id.dtend_text_view);
        }
    }

    public void setEvents(List<Event> events) {
        eventList.clear();
        eventList.addAll(events);
        notifyDataSetChanged();
    }


}