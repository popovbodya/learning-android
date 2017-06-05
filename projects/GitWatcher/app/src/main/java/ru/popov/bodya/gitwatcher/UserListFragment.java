package ru.popov.bodya.gitwatcher;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class UserListFragment extends Fragment {

    private static final String TAG = UserListFragment.class.toString();

    private ListAdapter adapter;
    private ListView listView;
    private UserStorage userStorage;

    public static UserListFragment newInstance(UserStorage storage) {
        return new UserListFragment();
    }

    @Override
    public void onAttach(Context context) {
        Log.e(TAG, "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        userStorage = ((GitWatcherApplication) getContext().getApplicationContext()).getUserStorage();
        adapter = new ListAdapter();
        userStorage.addNewListener(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        listView = (ListView) inflater.inflate(R.layout.fragment_list, container, false);
        adapter.setUserList(userStorage.getUserSet());
        listView.setAdapter(adapter);
        return listView;
    }


}
