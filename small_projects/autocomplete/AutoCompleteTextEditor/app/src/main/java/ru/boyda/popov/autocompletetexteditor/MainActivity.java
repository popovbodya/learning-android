package ru.boyda.popov.autocompletetexteditor;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private AutoCompleteTextView mAutoCompleteTextView;

    private List<String> mList;
    private ArrayAdapter<String> mAutoCompleteAdapter;
    private TextView mAutoListTextView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        prepareList();

        mAutoCompleteAdapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.dropdown_layout, mList);

        mAutoCompleteTextView.setAdapter(mAutoCompleteAdapter);

        mAutoListTextView = (TextView) findViewById(R.id.textViewAutoList);

        mButton = (Button) findViewById(R.id.buttonOk);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAdd = mAutoCompleteTextView.getText().toString();

                if (!mList.contains(newAdd)) {
                    mList.add(newAdd);
                    mAutoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.dropdown_layout, mList);
                    mAutoCompleteTextView.setAdapter(mAutoCompleteAdapter);
                }

            }
        });
    }

    private void prepareList() {

        mList = new ArrayList<>();
    }

}