package ru.boyda.popov.searchcinemas.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ru.boyda.popov.searchcinemas.CinemasAdapter;
import ru.boyda.popov.searchcinemas.R;
import ru.boyda.popov.searchcinemas.interfaces.CinemaDetailsHost;
import ru.boyda.popov.searchcinemas.interfaces.CinemaDetailsListener;
import ru.boyda.popov.searchcinemas.LoadTask;
import ru.boyda.popov.searchcinemas.parser.desc.CinemaDetails;

public class CinemasListFragment extends Fragment implements CinemaDetailsListener {

    private ListView listView;
    private View progressBar;
    private List<CinemaDetails> cinemaDetailsList;
    private LoadTask loadTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (cinemaDetailsList == null || loadTask == null) {
            loadTask = new LoadTask(this);
            loadTask.execute();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_cinemas, container, false);
        listView = (ListView) root.findViewById(R.id.cinemas_list);
        progressBar = root.findViewById(R.id.progress_bar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CinemaDetails details = ((CinemasAdapter) parent.getAdapter()).getItem(position);
                ((CinemaDetailsHost) getActivity()).displayCinemaWithDetails(details);
            }
        });

        showContent();
        return root;
    }

    private void showContent() {
        if (cinemaDetailsList != null) {
            listView.setAdapter(new CinemasAdapter(cinemaDetailsList));
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onReady(List<CinemaDetails> result) {
        cinemaDetailsList = result;
        if (isVisible()) {
            showContent();
        }
    }
}
