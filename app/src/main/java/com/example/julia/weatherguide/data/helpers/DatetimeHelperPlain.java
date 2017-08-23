package com.example.julia.weatherguide.data.helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DatetimeHelperPlain implements DatetimeHelper {

    public static final String DATABASE_DATE_PATTERN = "yyyy-MM-dd";
    private static final String UPDATE_DATE_PATTERN = "dd/MM HH:mm";


    @Override
    public List<String> getNextDates(int count) {
        List<String> result = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATABASE_DATE_PATTERN, Locale.getDefault());
        simpleDateFormat.setTimeZone(calendar.getTimeZone());
        calendar.getTime();

        for (int i = 0; i < count; ++i) {
            result.add(simpleDateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.getTime();
        }

        return result;
    }

    @Override
    public String getTimeForUpdate(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(UPDATE_DATE_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(TimeUnit.SECONDS.toMillis(timestamp));
    }

    @Override
    public String getDateFromTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.getTime();
        calendar.setTimeInMillis(TimeUnit.SECONDS.toMillis(timestamp));
        calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATABASE_DATE_PATTERN, Locale.getDefault());
        simpleDateFormat.setTimeZone(calendar.getTimeZone());
        return simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public DayOfMonth getTimeForPrediction(String date) {
        String[] strings = date.split("-");
        int month = Integer.valueOf(strings[1]);
        int day = Integer.valueOf(strings[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.getTime();

        int calendarMonth = calendar.get(Calendar.MONTH);
        int calendarDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        boolean isToday = false;
        boolean isTomorrow = false;

        Calendar today = Calendar.getInstance();
        today.setTimeZone(TimeZone.getTimeZone("GMT"));
        today.getTime();
        isToday = calendarMonth == today.get(Calendar.MONTH)
            && calendarDayOfMonth == today.get(Calendar.DAY_OF_MONTH);

        if (!isToday) {
            today.add(Calendar.DAY_OF_YEAR, 1);
            today.getTime();
            isTomorrow = calendarMonth == today.get(Calendar.MONTH)
                && calendarDayOfMonth == today.get(Calendar.DAY_OF_MONTH);
        }

        return new DayOfMonth(calendarMonth, calendarDayOfMonth, isToday, isTomorrow);
    }

    @Override
    public DayOfMonth getTimeForPrediction(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(TimeUnit.SECONDS.toMillis(timestamp));
        calendar.getTime();

        int calendarMonth = calendar.get(Calendar.MONTH);
        int calendarDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        boolean isToday = false;
        boolean isTomorrow = false;

        Calendar today = Calendar.getInstance();
        today.setTimeZone(TimeZone.getTimeZone("GMT"));
        today.getTime();
        isToday = calendarMonth == today.get(Calendar.MONTH)
            && calendarDayOfMonth == today.get(Calendar.DAY_OF_MONTH);

        if (!isToday) {
            today.add(Calendar.DAY_OF_YEAR, 1);
            today.getTime();
            isTomorrow = calendarMonth == today.get(Calendar.MONTH)
                && calendarDayOfMonth == today.get(Calendar.DAY_OF_MONTH);
        }

        return new DayOfMonth(calendarMonth, calendarDayOfMonth, isToday, isTomorrow);
    }

    @Override
    public long getCurrentTimestampGMT() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT"));
        now.getTime();
        return TimeUnit.MILLISECONDS.toSeconds(now.getTimeInMillis());
    }

    public static class DayOfMonth {

        // [0..11]
        public final int month;
        public final int day;
        public final boolean isToday;
        public final boolean isTomorrow;

        public DayOfMonth(int month, int day, boolean isToday, boolean isTomorrow) {
            this.month = month;
            this.day = day;
            this.isToday = isToday;
            this.isTomorrow = isTomorrow;
        }
    }
}
