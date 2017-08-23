package com.example.julia.weatherguide.data.helpers;

import java.util.List;
import static com.example.julia.weatherguide.data.helpers.DatetimeHelperPlain.DayOfMonth;

public interface DatetimeHelper {

    List<String> getNextDates(int count);

    String getTimeForUpdate(long timestamp);

    DayOfMonth getTimeForPrediction(long timestamp);

    long getCurrentTimestampGMT();

    String getDateFromTimestamp(long timestamp);

    DayOfMonth getTimeForPrediction(String date);

}
