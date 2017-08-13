package com.example.julia.weatherguide.data.helpers;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import static com.example.julia.weatherguide.data.helpers.DatetimeHelperPlain.DayOfMonth;


public class DatetimeHelperTest {

    private static final int DATES_COUNT = 10;
    private DatetimeHelper datetimeHelper;

    @Before
    public void before() {
        datetimeHelper = new DatetimeHelperPlain();
    }

    @Test
    public void getNextDates_count() {
        List<String> dates = datetimeHelper.getNextDates(DATES_COUNT);

        assertEquals(dates.size(), DATES_COUNT);
    }

    @Test
    public void getTimeForUpdate_nonNull() {
        String time = datetimeHelper.getTimeForUpdate(datetimeHelper.getCurrentTimestampGMT());
        assertNotNull(time);
    }

    @Test
    public void getCurrentTimestamp_getTimeForPrediction_isToday() {
        DatetimeHelperPlain.DayOfMonth dayOfMonth = datetimeHelper.getTimeForPrediction(
                datetimeHelper.getCurrentTimestampGMT()
        );
        assertNotNull(dayOfMonth);
        assertTrue(dayOfMonth.isToday);
        assertFalse(dayOfMonth.isTomorrow);
    }

    @Test
    public void dateIsConsistent() {
        long timestamp = datetimeHelper.getCurrentTimestampGMT();
        String date = datetimeHelper.getDateFromTimestamp(timestamp);

        DayOfMonth dayFromTimestamp = datetimeHelper.getTimeForPrediction(timestamp);
        DayOfMonth dayFromDate = datetimeHelper.getTimeForPrediction(date);

        assertEquals(dayFromDate.day, dayFromTimestamp.day);
        assertEquals(dayFromDate.month, dayFromTimestamp.month);
        assertEquals(dayFromDate.isTomorrow, dayFromTimestamp.isTomorrow);
        assertEquals(dayFromDate.isToday, dayFromTimestamp.isToday);
    }

}
