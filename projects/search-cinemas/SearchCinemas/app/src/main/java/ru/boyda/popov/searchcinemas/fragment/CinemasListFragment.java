package ru.boyda.popov.searchcinemas.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.boyda.popov.searchcinemas.CinemaDetailsType;
import ru.boyda.popov.searchcinemas.CinemasAdapter;
import ru.boyda.popov.searchcinemas.Loader;
import ru.boyda.popov.searchcinemas.R;
import ru.boyda.popov.searchcinemas.interfaces.CinemaDetailsHost;
import ru.boyda.popov.searchcinemas.interfaces.CinemaDetailsListener;
import ru.boyda.popov.searchcinemas.LoadTask;
import ru.boyda.popov.searchcinemas.parser.geo.CinemaDetails;

public class CinemasListFragment extends Fragment implements CinemaDetailsListener, Loader.Callback {

    private ListView listView;
    private View progressBar;
    private List<CinemaDetails> cinemaDetailsList;
    private LoadTask loadTask;
    private CinemaDetailsType cinemaDetailsType;
    private TextView errorTextView;
    private Button tryAgainButton;
    private Button searchButton;

    private Loader mWorkerThread;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mWorkerThread = new Loader(new Handler(), this);
        mWorkerThread.start();
        mWorkerThread.prepareHandler();

        tryToLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_cinemas, container, false);

        listView = (ListView) root.findViewById(R.id.cinemas_list);
        progressBar = root.findViewById(R.id.progress_bar);
        tryAgainButton = (Button) root.findViewById(R.id.button_try_again);
        errorTextView = (TextView) root.findViewById(R.id.text_view_error);
        searchButton = (Button) root.findViewById(R.id.search_button_fragment);

        Switch typeSwitch = (Switch) root.findViewById(R.id.monitored_switch);


        typeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cinemaDetailsType = CinemaDetailsType.MapView;

                } else {
                    cinemaDetailsType = CinemaDetailsType.CardView;
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                tryToLoad();
            }
        });

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showError(false);
                tryToLoad();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CinemaDetails details = ((CinemasAdapter) parent.getAdapter()).getItem(position);
                ((CinemaDetailsHost) getActivity()).displayCinemaWithDetails(details, cinemaDetailsType);
            }
        });

        showContent();
        showError(false);

        return root;
    }

    private void tryToLoad() {
        if (cinemaDetailsList == null || loadTask == null) {
//            loadTask = new LoadTask(this);
//            loadTask.execute();
            mWorkerThread.queueTask();
        }
    }

    private void showContent() {
        if (cinemaDetailsList != null) {
            listView.setAdapter(new CinemasAdapter(cinemaDetailsList));
            showProgress(false);
        } else {
            showProgress(true);
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(boolean show) {
        errorTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        tryAgainButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onReady(List<CinemaDetails> result) {

        loadTask = null;

        if (result == null || result.size() == 0) {
            showError(true);
            showProgress(false);
        } else {
            cinemaDetailsList = result;
            if (isVisible()) {
                showContent();
            }
        }
    }


    @Override
    public void onCinemasDownloaded(List<CinemaDetails> cinemaDetailsList) {
        if (cinemaDetailsList != null) {

            Toast.makeText(getContext(), "new cinemas downloaded", Toast.LENGTH_LONG).show();

            if (this.cinemaDetailsList == null) {
                this.cinemaDetailsList = new ArrayList<>();
            }
            this.cinemaDetailsList.addAll(cinemaDetailsList);
            if (isVisible()) {
                showContent();
            }
        }
    }

    @Override
    public void onDestroy() {
        mWorkerThread.quit();
        super.onDestroy();
    }

}
