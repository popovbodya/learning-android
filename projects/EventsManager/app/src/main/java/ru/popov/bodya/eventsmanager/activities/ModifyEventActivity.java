package ru.popov.bodya.eventsmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.Calendar;

import ru.popov.bodya.eventsmanager.DateHelper;
import ru.popov.bodya.eventsmanager.DatePickerFragment;
import ru.popov.bodya.eventsmanager.Event;
import ru.popov.bodya.eventsmanager.R;
import ru.popov.bodya.eventsmanager.TimePickerFragment;

public class ModifyEventActivity extends AppCompatActivity implements DatePickerFragment.onDateListener, TimePickerFragment.OnTimeListener {

    private static final String TAG = ModifyEventActivity.class.getName();
    private static final String UPDATE_MODE_KEY = "update_mode";
    private static final String DATE_PICKER_TAG = "datePicker";
    private static final String TIME_PICKER_TAG = "timePicker";
    private static final byte START_TIME_KEY = 0;
    private static final byte END_TIME_KEY = 1;

    private TextInputEditText titleEditText;
    private TextInputEditText descEditText;
    private TextInputEditText[] editTexts;
    private ImageButton startTimeImageButton;
    private ImageButton endTimeImageButton;
    private Button modifyButton;

    private boolean updateMode;

    public static Intent newIntent(Context context) {
        return new Intent(context, ModifyEventActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        titleEditText = (TextInputEditText) findViewById(R.id.title_edit_text);
        descEditText = (TextInputEditText) findViewById(R.id.desc_edit_text);
        editTexts = new TextInputEditText[]{titleEditText, descEditText};
        for (TextInputEditText editText : editTexts) {
            editText.addTextChangedListener(new EditTextWatcherImpl());
        }

        startTimeImageButton = (ImageButton) findViewById(R.id.pick_time_text_view);
        endTimeImageButton = (ImageButton) findViewById(R.id.pick_end_time_text_view);
        modifyButton = (Button) findViewById(R.id.modify_event_button);


        startTimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(DateHelper.TimeMode.Start);
            }
        });

        endTimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(DateHelper.TimeMode.End);
            }
        });

        Serializable eventWithUpdate = getIntent().getSerializableExtra(UPDATE_MODE_KEY);
        if (eventWithUpdate != null) {
            Event extrasEvent = (Event) eventWithUpdate;
            changeContentInViews(extrasEvent);
            this.updateMode = true;
        }

    }
    @Override
    public void onDatePicked(Calendar calendar, DateHelper.TimeMode timeMode) {
        Log.e(TAG, "onDatePicked with calendar: " + calendar.toString() + " with timeMode: " + timeMode);
        showTimePickerDialog(timeMode, calendar);
    }

    @Override
    public void onTimePicked(Calendar calendar, DateHelper.TimeMode timeMode) {
        Log.e(TAG, "onTimePicked with timeMode: " + timeMode);
        Log.e(TAG, "onTimePicked with calendar: " + DateHelper.getDateInFormat(calendar.getTimeInMillis()));
    }

    private void showDatePickerDialog(DateHelper.TimeMode timeMode) {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(timeMode);
        datePickerFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }

    private void showTimePickerDialog(DateHelper.TimeMode timeMode, Calendar calendar) {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(timeMode, calendar);
        timePickerFragment.show(getSupportFragmentManager(), TIME_PICKER_TAG);
    }


    private void changeContentInViews(Event event) {
        if (event == null) {
            return;
        }
        titleEditText.setText(event.getTitle());
        descEditText.setText(String.valueOf(event.getDescription()));
        modifyButton.setText(getResources().getString(R.string.modify_event));
    }


    private class EditTextWatcherImpl implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean setEnabled = true;
            for (EditText editText : editTexts) {
                Editable text = editText.getText();
                if (TextUtils.isEmpty(text)) {
                    setEnabled = false;
                    break;
                }
            }
            modifyButton.setEnabled(setEnabled);
        }
    }


}
