package com.example.julia.weatherguide.data.services.refresh;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;

public class RefreshDatabaseJob extends Job {

    public static final String TAG = RefreshDatabaseJob.class.getSimpleName();

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        startService(getContext());
        return Job.Result.SUCCESS;
    }

    protected void startService(Context context) {
        context.startService(new Intent(context, RefreshDatabaseService.class));
    }

}