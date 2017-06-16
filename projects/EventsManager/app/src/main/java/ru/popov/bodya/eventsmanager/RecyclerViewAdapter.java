package ru.popov.bodya.eventsmanager;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.popov.bodya.eventsmanager.activities.ModifyEventActivity;
import ru.popov.bodya.eventsmanager.interfaces.ModelProvider;
import ru.popov.bodya.eventsmanager.model.EditModeHolder;
import ru.popov.bodya.eventsmanager.model.Event;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecyclerViewAdapter.class.getName();
    private static final String UPDATE_MODE_KEY = "update_mode";

    private List<Event> eventList;
    private Context context;
    private ModelProvider provider;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        eventList = new ArrayList<>();
        provider = (ModelProvider) context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_elem_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Event event = eventList.get(i);

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
        viewHolder.dateStart.setText(context.getResources().getString(R.string.event_start, startDateFromMills));

        String endDateFromMills = DateHelper.getDateInFormat(Long.valueOf(event.getDateEnd()));
        viewHolder.dateEnd.setText(context.getResources().getString(R.string.event_end, endDateFromMills));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onItemClick with event: " + event.getTitle());
                EditModeHolder.EditMode editMode = provider.getEditModeHolder().getEditMode();

                switch (editMode) {
                    case Update:
                        Log.e(TAG, "onItemClick with update mode");
                        Intent intent = ModifyEventActivity.newIntent(context);
                        intent.putExtra(UPDATE_MODE_KEY, event);
                        context.startActivity(intent);
                        break;
                    case Delete:
                        Log.e(TAG, "onItemClick with delete mode");
                        provider.getDataBaseWorker().queueTask(new Runnable() {
                            @Override
                            public void run() {
                                provider.getEventStorage().deleteEvent(event);
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView dateStart;
        private TextView dateEnd;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            description = (TextView) itemView.findViewById(R.id.description_text_view);
            dateStart = (TextView) itemView.findViewById(R.id.dtstart_text_view);
            dateEnd = (TextView) itemView.findViewById(R.id.dtend_text_view);
        }
    }

    public void setEvents(List<Event> events) {
        eventList.clear();
        eventList.addAll(events);
        notifyDataSetChanged();
    }


}