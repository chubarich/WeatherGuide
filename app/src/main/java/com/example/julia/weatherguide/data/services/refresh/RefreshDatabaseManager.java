package com.example.julia.weatherguide.data.services.refresh;


import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

public class RefreshDatabaseManager {

    private static final int MIN_PERIOD_IN_MILLIS = 300001;

    public static void setCurrentRefreshPolicy(Period period) {
        if (period != null) {
            JobManager.instance().cancelAllForTag(RefreshDatabaseJob.TAG);

            if (period != Period.DISABLED) {
                new JobRequest.Builder(RefreshDatabaseJob.TAG)
                    .setPeriodic(getMillisecondPeriod(period), MIN_PERIOD_IN_MILLIS)
                    .setPersisted(true)
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .build()
                    .schedule();
            }
        }
    }

    public static Period getPeriod(int positionInAscendingList) {
        switch (positionInAscendingList) {
            case 0: return Period.DISABLED;
            case 1: return Period.HALF_AN_HOUR;
            case 2: return Period.ONE_HOUR;
            case 3: return Period.THREE_HOURS;
            case 4: return Period.SIX_HOURS;
            case 5: return Period.TWELVE_HOURS;
            case 6: return Period.TWENTY_FOUR_HOURS;
            default: throw new IllegalStateException("Unknown period");
        }
    }

    private static long getMillisecondPeriod(Period period) {
        if (period == Period.DISABLED) throw new IllegalStateException("Cannot convert DISABLED period into duration");
        return TimeUnit.MINUTES.toMillis(period.getMinutes());
    }

    // --------------------------------------- inner classes ----------------------------------------

    public enum Period {
        DISABLED(-1),
        HALF_AN_HOUR(30),
        ONE_HOUR(60),
        THREE_HOURS(180),
        SIX_HOURS(360),
        TWELVE_HOURS(720),
        TWENTY_FOUR_HOURS(1440);

        private final long minutes;

        Period(long minutes) {
            this.minutes = minutes;
        }

        public long getMinutes() {
            return minutes;
        }
    }
}