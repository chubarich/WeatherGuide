package com.example.julia.weatherguide.interactors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.julia.weatherguide.repositories.CurrentWeatherDataModel;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.services.current_weather_refresh.CurrentWeatherRefreshDataService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by julia on 15.07.17.
 */

public class CurrentWeatherInteractorImpl implements CurrentWeatherInteractor {

    private static final String JOB_TAG = "refresh_service";
    private static final int WINDOW_IN_MINUTES = 30;

    private CurrentWeatherRepository repository;
    private Context context;

    public CurrentWeatherInteractorImpl(@NonNull Context context, CurrentWeatherRepository repo) {
        repository = repo;
        this.context = context;
    }

    @Override
    public Observable<CurrentWeatherDataModel> getCurrentWeatherForLocation(@NonNull String location) {
        return repository.getCurrentWeatherForLocation(location);
    }

    @Override
    public Observable<CurrentWeatherDataModel> getFreshCurrentWeatherForLocation(@NonNull String location) {
        return repository.getFreshCurrentWeatherForLocation(location);
    }


    @Override
    public String getCurrentLocation() {
        return repository.getCurrentLocation();
    }


    @Override
    public void scheduleForUpdateCurrentWeather( int interval ) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job job = dispatcher.newJobBuilder()
                .setService(CurrentWeatherRefreshDataService.class)
                .setTag(JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTrigger(Trigger.executionWindow((int) TimeUnit.MINUTES.toMillis(interval),
                        (int) TimeUnit.MINUTES.toMillis(interval + WINDOW_IN_MINUTES) ))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(job);
    }
}
