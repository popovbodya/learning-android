package ru.dimasokol.currencies;

import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ru.dimasokol.currencies.networking.CurrenciesList;
import ru.dimasokol.currencies.networking.Currency;


public class FragmentList extends Fragment {

    private static String ARG_LIST = "LIST";

    private CurrenciesList mCurrencies;
    private CurrenciesAdapter mAdapter;
    private ListView mListView;

    public static FragmentList newInstance(CurrenciesList list) {
        FragmentList fragment = new FragmentList();
        fragment.mCurrencies = list;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_list, container, false);
        mAdapter = new CurrenciesAdapter(mCurrencies);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Currency currency = mAdapter.getItem(position);
                Intent calculator = new Intent(getActivity(), CalculatorActivity.class);
                calculator.putExtra(CalculatorActivity.EXTRA_CODE, currency.getCharCode());
                startActivity(calculator);
            }
        });
        return mListView;
    }


}
