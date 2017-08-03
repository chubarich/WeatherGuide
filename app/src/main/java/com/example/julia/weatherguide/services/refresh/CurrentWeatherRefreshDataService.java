package com.example.julia.weatherguide.services.refresh;

import android.util.Log;

import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.presentation.application.WeatherGuideApplication;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

public class CurrentWeatherRefreshDataService extends JobService {

    private final static String TAG = CurrentWeatherRefreshDataService.class.getSimpleName();

    @Inject
    WeatherRepository weatherRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        ((WeatherGuideApplication) getApplication()).getCurrentWeatherComponent()
            .inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        weatherRepository.getFreshWeather()
            .subscribeOn(Schedulers.io())
            .subscribe(
                data -> {
                    jobFinished(job, false);
                    Log.d(TAG, "onStartJob get data ");
                },
                error -> Log.d(TAG, "onStartJob error " + error.getLocalizedMessage())
            );
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

}
