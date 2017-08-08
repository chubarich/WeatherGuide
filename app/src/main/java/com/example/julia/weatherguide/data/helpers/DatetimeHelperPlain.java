package com.example.julia.weatherguide.data.helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DatetimeHelperPlain implements DatetimeHelper {

    public static final String DATE_PATTERN = "yyyy-MM-dd";


    @Override
    public List<String> getNextDates(int count) {
        List<String> result = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        simpleDateFormat.setTimeZone(calendar.getTimeZone());

        for (int i = 0; i < count; ++i) {
            result.add(simpleDateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.getTime();
        }

        return result;
    }

}
