package ru.popov.bodya.eventsmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DateHelperTest {

    private Calendar calendar;

    @Before
    public void prepare() {
        calendar = Calendar.getInstance();
        System.out.println(calendar.getTimeInMillis());
    }

    @Test
    public void testCorrectChangingTimeInCalendar() throws Exception {

        int expectedHoursAmount = 3;
        int expectedMinuteAmount = 25;

        DateHelper.changeTimeInCalendar(calendar, expectedHoursAmount, expectedMinuteAmount);

        assertThat(expectedHoursAmount, is(calendar.get(Calendar.HOUR_OF_DAY)));
        assertThat(expectedMinuteAmount, is(calendar.get(Calendar.MINUTE)));
    }

}