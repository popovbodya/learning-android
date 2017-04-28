package ru.boyda.popov.currencyconverter.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Утилиты форматирования дат
 */
public final class DateFormatUtils {

    private static SimpleDateFormat sRequestFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static SimpleDateFormat sResponseFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    private DateFormatUtils() {

    }

    @NonNull
    public static String dateForRequest(Date date) {
        return sRequestFormat.format(date);
    }

    @Nullable
    public static Date dateFromString(String string) {
        try {
            return sResponseFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}