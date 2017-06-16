package ru.popov.bodya.eventsmanager.fragments;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

import ru.popov.bodya.eventsmanager.DateHelper;
import ru.popov.bodya.eventsmanager.model.TimeMode;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = TimePickerFragment.class.getName();
    private static final String TIME_MODE_KEY = "time_mode_key";
    private static final String CALENDAR_KEY = "calendar_key";
    private OnTimeListener onTimeListener;
    private Calendar calendar;
    private TimeMode timeMode;

    public interface OnTimeListener {
        void onTimePicked(Calendar calendar, TimeMode timeMode);
    }

    public static TimePickerFragment newInstance(TimeMode timeMode, Calendar calendar) {
        Log.e(TAG, "newInstance");
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(TIME_MODE_KEY, timeMode);
        args.putSerializable(CALENDAR_KEY, calendar);
        timePickerFragment.setArguments(args);
        return timePickerFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach");
        timeMode = (TimeMode) getArguments().getSerializable(TIME_MODE_KEY);
        calendar = (Calendar) getArguments().getSerializable(CALENDAR_KEY);
        onTimeListener = (OnTimeListener) getActivity();
    }

    @Override
    public void onDetach() {
        Log.e(TAG, "onDetach");
        onTimeListener = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.e(TAG, "onCreateDialog");
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.e(TAG, "onTimeSet with time: hours = " + hourOfDay + ", minute = " + minute);
        DateHelper.changeTimeInCalendar(calendar, hourOfDay, minute);
        onTimeListener.onTimePicked(calendar, timeMode);
    }
}
