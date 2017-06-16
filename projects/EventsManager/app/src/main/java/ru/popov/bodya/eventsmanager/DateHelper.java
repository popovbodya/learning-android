package ru.popov.bodya.eventsmanager;


import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public enum TimeMode implements Serializable {
        Start,
        End
    }

    public static String getDateInFormat(long mills) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.ENGLISH);
        return dateFormat.format(getDateFromMills(mills));
    }

    public static void changeTimeInCalendar(Calendar calendar, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        Log.e("DateHelper", "changeTimeInCalendar: " + getDateInFormat(calendar.getTimeInMillis()));
    }

    private static Date getDateFromMills(long mills) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(mills);
        return instance.getTime();
    }


}
