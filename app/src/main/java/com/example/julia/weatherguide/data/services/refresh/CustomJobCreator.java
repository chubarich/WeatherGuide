package com.example.julia.weatherguide.data.services.refresh;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;


public class CustomJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        return tag.equals(RefreshDatabaseJob.TAG) ? new RefreshDatabaseJob() : null;
    }

}
