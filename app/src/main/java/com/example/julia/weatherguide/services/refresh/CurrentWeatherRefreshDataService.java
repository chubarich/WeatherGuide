package com.example.julia.weatherguide.services.refresh;


import android.util.Log;

import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;


/**
 * Created by julia on 15.07.17.
 */

public class CurrentWeatherRefreshDataService extends JobService {

    @Inject
    CurrentWeatherRepository repository;

    private static final String TAG = CurrentWeatherRefreshDataService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters job) {
        repository.getFreshCurrentWeatherForLocation(repository.getCurrentLocation())
                .subscribeOn(Schedulers.io()).subscribe(data -> {
                    //TODO: print to log and skip this data
                    Log.d(TAG, "onStartJob get data ");
                 }, error -> {
                    //TODO: print this error to log
                    Log.d(TAG, "onStartJob error " + error.getLocalizedMessage());
                 }, () -> jobFinished(job, false)
                );
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WeatherGuideApplication.getDataComponent().inject(this);
    }
}
