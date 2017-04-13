package ru.popovbodya.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GuessNumberGameActivity extends Activity {




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess_number_game);

        Attempt.counter = 0;

        Button goBackButton = (Button) findViewById(R.id.back_toMain_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button addButton = (Button) findViewById(R.id.try_button);
        final ListView items = (ListView) findViewById(R.id.attempts);
        final AttemptsAdapter adapter = new AttemptsAdapter();
        items.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText);
                adapter.add(new Attempt(Integer.parseInt(editText.getText().toString())));
            }
        });

    }
    private class AttemptsAdapter extends ArrayAdapter<Attempt> implements ListAdapter {

        public AttemptsAdapter() {
            super(GuessNumberGameActivity.this, R.layout.attempts_list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final View view = getLayoutInflater().inflate(R.layout.attempts_list, null);
            final Attempt attempt = getItem(position);
            String attemptCounterToSet = "";
            String proposedNumberToSet = "";
            if (attempt != null) {
                attemptCounterToSet = Attempt.TEMPLATE + attempt.attempt_number;
                proposedNumberToSet = String.valueOf(attempt.number);
            }
            ((TextView) view.findViewById(R.id.attempt_counter_value)).setText(attemptCounterToSet);
            ((TextView) view.findViewById(R.id.proposed_number_text_block)).setText(proposedNumberToSet);
            return view;
        }
    }

    private static class Attempt {
        private static final String TEMPLATE = "Попытка # ";
        private static int counter = 0;
        private int number;
        private int attempt_number;

        public Attempt(int number) {
            counter++;
            this.number = number;
            this.attempt_number = counter;
        }
    }



}
