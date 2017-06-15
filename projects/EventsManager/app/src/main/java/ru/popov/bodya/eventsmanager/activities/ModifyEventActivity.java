package ru.popov.bodya.eventsmanager.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;

import ru.popov.bodya.eventsmanager.DatePickerFragment;
import ru.popov.bodya.eventsmanager.Event;
import ru.popov.bodya.eventsmanager.R;

public class ModifyEventActivity extends AppCompatActivity {

    private static final String UPDATE_MODE_KEY = "update_mode";
    private static final String DATE_PICKER_TAG = "datePicker";
    private static final String TIME_PICKER_TAG = "timePicker";
    private static final byte START_TIME_KEY = 0;
    private static final byte END_TIME_KEY = 1;

    private TextInputEditText titleEditText;
    private TextInputEditText descEditText;
    private ImageButton startTimeImageButton;
    private ImageButton endTimeImageButton;
    private Button modifyButton;

    private boolean updateMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        titleEditText = (TextInputEditText) findViewById(R.id.title_edit_text);
        descEditText = (TextInputEditText) findViewById(R.id.desc_edit_text);
        startTimeImageButton = (ImageButton) findViewById(R.id.pick_time_text_view);
        endTimeImageButton = (ImageButton) findViewById(R.id.pick_end_time_text_view);
        modifyButton = (Button) findViewById(R.id.modify_event_button);


        startTimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Serializable eventWithUpdate = getIntent().getSerializableExtra(UPDATE_MODE_KEY);
        if (eventWithUpdate != null) {
            Event extrasEvent = (Event) eventWithUpdate;
            changeContentInViews(extrasEvent);
            this.updateMode = true;
        }

    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
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
