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
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

import ru.popov.bodya.eventsmanager.DateHelper;
import ru.popov.bodya.eventsmanager.model.EventStorage;
import ru.popov.bodya.eventsmanager.model.TimeMode;
import ru.popov.bodya.eventsmanager.db.DataBaseWorker;
import ru.popov.bodya.eventsmanager.fragments.DatePickerFragment;
import ru.popov.bodya.eventsmanager.model.Event;
import ru.popov.bodya.eventsmanager.R;
import ru.popov.bodya.eventsmanager.fragments.TimePickerFragment;
import ru.popov.bodya.eventsmanager.interfaces.ModelProvider;

public class ModifyEventActivity extends AppCompatActivity implements DatePickerFragment.onDateListener, TimePickerFragment.OnTimeListener {

    private static final String TAG = ModifyEventActivity.class.getName();
    private static final String UPDATE_MODE_KEY = "update_mode";
    private static final String DATE_PICKER_TAG = "datePicker";
    private static final String TIME_PICKER_TAG = "timePicker";
    private static final int ONE_HOUR_IN_MILLIS = 3600000;

    private TextInputEditText titleEditText;
    private TextInputEditText descEditText;
    private TextInputEditText[] editTexts;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private Button modifyButton;

    private DataBaseWorker dataBaseWorker;
    private EventStorage storage;
    private Event cachedEvent;
    private boolean updateMode;

    public static Intent newIntent(Context context) {
        return new Intent(context, ModifyEventActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        ModelProvider provider = (ModelProvider) getApplication();
        dataBaseWorker = provider.getDataBaseWorker();
        storage = provider.getEventStorage();

        titleEditText = (TextInputEditText) findViewById(R.id.title_edit_text);
        descEditText = (TextInputEditText) findViewById(R.id.desc_edit_text);
        editTexts = new TextInputEditText[]{titleEditText, descEditText};
        for (TextInputEditText editText : editTexts) {
            editText.addTextChangedListener(new EditTextWatcherImpl());
        }

        startTimeTextView = (TextView) findViewById(R.id.picked_start_time_text_view);
        endTimeTextView = (TextView) findViewById(R.id.picked_end_time_text_view);
        modifyButton = (Button) findViewById(R.id.modify_event_button);
        ImageButton startTimeImageButton = (ImageButton) findViewById(R.id.pick_start_time_image_button);
        ImageButton endTimeImageButton = (ImageButton) findViewById(R.id.pick_end_time_image_button);

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateMode) {
                    updateEventInDb();
                } else {
                    addEventToDb();
                }
            }
        });

        startTimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(TimeMode.Start);
            }
        });

        endTimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(TimeMode.End);
            }
        });

        Serializable eventWithUpdate = getIntent().getSerializableExtra(UPDATE_MODE_KEY);
        if (eventWithUpdate != null) {
            cachedEvent = (Event) eventWithUpdate;
            changeContentInViews(cachedEvent);
            this.updateMode = true;
        } else {
            cachedEvent = new Event();
        }
    }

    private void updateEventInDb() {
        cachedEvent.setTitle(titleEditText.getText().toString());
        cachedEvent.setDescription(descEditText.getText().toString());
        dataBaseWorker.queueTask(new Runnable() {
            @Override
            public void run() {
                storage.updateEvent(cachedEvent);
            }
        });
        finish();
    }

    private void addEventToDb() {
        cachedEvent.setTitle(titleEditText.getText().toString());
        cachedEvent.setDescription(descEditText.getText().toString());
        if (TextUtils.isEmpty(cachedEvent.getDateStart())) {
            long timeInMillis = Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis();
            cachedEvent.setDateStart(String.valueOf(timeInMillis));
        }
        if (TextUtils.isEmpty(cachedEvent.getDateEnd())) {
            long timeInMillis = Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis() + ONE_HOUR_IN_MILLIS;
            cachedEvent.setDateEnd(String.valueOf(timeInMillis));
        }
        dataBaseWorker.queueTask(new Runnable() {
            @Override
            public void run() {
                storage.addEvent(cachedEvent);
            }
        });
        finish();
    }

    @Override
    public void onDatePicked(Calendar calendar, TimeMode timeMode) {
        Log.e(TAG, "onDatePicked with calendar: " + calendar.toString() + " with timeMode: " + timeMode);
        showTimePickerDialog(timeMode, calendar);
    }

    @Override
    public void onTimePicked(Calendar calendar, TimeMode timeMode) {
        Log.e(TAG, "onTimePicked with timeMode: " + timeMode);
        Log.e(TAG, "onTimePicked with calendar: " + DateHelper.getDateInFormat(calendar.getTimeInMillis()));

        if (timeMode == TimeMode.Start) {
            cachedEvent.setDateStart(String.valueOf(calendar.getTimeInMillis()));
            startTimeTextView.setText(DateHelper.getDateInFormat(calendar.getTimeInMillis()));
        } else if (timeMode == TimeMode.End) {
            cachedEvent.setDateEnd(String.valueOf(calendar.getTimeInMillis()));
            endTimeTextView.setText(DateHelper.getDateInFormat(calendar.getTimeInMillis()));
        }
    }

    private void showDatePickerDialog(TimeMode timeMode) {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(timeMode);
        datePickerFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
    }

    private void showTimePickerDialog(TimeMode timeMode, Calendar calendar) {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(timeMode, calendar);
        timePickerFragment.show(getSupportFragmentManager(), TIME_PICKER_TAG);
    }


    private void changeContentInViews(Event event) {
        if (event == null) {
            return;
        }
        startTimeTextView.setText(DateHelper.getDateInFormat(Long.valueOf(event.getDateStart())));
        endTimeTextView.setText(DateHelper.getDateInFormat(Long.valueOf(event.getDateEnd())));
        titleEditText.setText(event.getTitle());
        descEditText.setText(event.getDescription());
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
