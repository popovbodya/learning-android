package ru.popov.bodya.eventsmanager.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ru.popov.bodya.eventsmanager.Event;
import ru.popov.bodya.eventsmanager.R;

public class ModifyEventActivity extends AppCompatActivity {

    private static final String UPDATE_MODE_KEY = "update_mode";

    private TextInputEditText titleEditText;
    private TextInputEditText descEditText;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private Button modifyButton;

    private boolean updateMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        titleEditText = (TextInputEditText) findViewById(R.id.title_edit_text);
        descEditText = (TextInputEditText) findViewById(R.id.desc_edit_text);
        startTimeTextView = (TextView) findViewById(R.id.pick_time_text_view);
        endTimeTextView = (TextView) findViewById(R.id.pick_end_time_text_view);
        modifyButton = (Button) findViewById(R.id.modify_event_button);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Event extrasEvent = (Event) extras.getSerializable(UPDATE_MODE_KEY);
            if (extrasEvent != null) {
               changeContentInViews(extrasEvent);
                updateMode = true;
            }
        }
    }

    private void changeContentInViews(Event event) {
        if (event == null) {
            return;
        }
        titleEditText.setText(event.getTitle());
        descEditText.setText(String.valueOf(event.getDescription()));
        modifyButton.setText(getResources().getString(R.string.modify_event));
    }
}
