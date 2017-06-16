package ru.popov.bodya.eventsmanager.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.TimeZone;

import ru.popov.bodya.eventsmanager.model.TimeMode;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DatePickerFragment.class.getName();
    private static final String TIME_MODE = "time mode";
    private onDateListener onDateListener;
    private TimeMode timeMode;

    public interface onDateListener {
        void onDatePicked(Calendar calendar, TimeMode timeMode);
    }

    public static DatePickerFragment newInstance(TimeMode timeMode) {
        Log.e(TAG, "newInstance");
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(TIME_MODE, timeMode);
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach");
        timeMode = (TimeMode) getArguments().getSerializable(TIME_MODE);
        onDateListener = (DatePickerFragment.onDateListener) getActivity();
    }

    @Override
    public void onDetach() {
        Log.e(TAG, "onDetach");
        onDateListener = null;
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.e(TAG, "onCreateDialog");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.e(TAG, "onTimeSet with time: year = " + year + ", month = " + month + ", day = " + day);
        Log.e(TAG, "TimeMode = " + timeMode);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(year, month, day);
        onDateListener.onDatePicked(calendar, timeMode);
    }
}