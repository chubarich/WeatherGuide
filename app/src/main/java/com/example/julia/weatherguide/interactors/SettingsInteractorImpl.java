package com.example.julia.weatherguide.interactors;

import android.content.Context;

import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;
import com.example.julia.weatherguide.services.refresh.CurrentWeatherRefreshDataService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Scheduler;

public class SettingsInteractorImpl implements SettingsInteractor {

    private static final String JOB_TAG = "refresh_service";
    private static final int WINDOW_IN_MINUTES = 30;

    private Context context;
    private CurrentWeatherRepository repository;
    private final Scheduler workerScheduler;
    private final Scheduler uiScheduler;

    public SettingsInteractorImpl(Context context, CurrentWeatherRepository currentWeatherRepository,
                                  Scheduler workerScheduler, Scheduler uiScheduler) {
        this.context = context.getApplicationContext();
        this.repository = currentWeatherRepository;
        this.workerScheduler = workerScheduler;
        this.uiScheduler = uiScheduler;
    }

    // ---------------------------------------- public ----------------------------------------------

    @Override
    public Completable saveRefreshPeriod(int period) {
        return Completable.fromAction(
            () -> scheduleForUpdateCurrentWeather(period)
        )
            .subscribeOn(workerScheduler)
            .observeOn(uiScheduler);
    }

    // ---------------------------------------- private ---------------------------------------------

    private void scheduleForUpdateCurrentWeather(int period) throws ExceptionBundle {
        if (!repository.isLocationInitialized())
            throw new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED);

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job job = dispatcher.newJobBuilder()
            .setService(CurrentWeatherRefreshDataService.class)
            .setTag(JOB_TAG)
            .setLifetime(Lifetime.FOREVER)
            .setRecurring(true)
            .setConstraints(Constraint.ON_ANY_NETWORK)
            .setTrigger(Trigger.executionWindow((int) TimeUnit.MINUTES.toSeconds(period),
                (int) TimeUnit.MINUTES.toSeconds(period + WINDOW_IN_MINUTES)))
            .setReplaceCurrent(true)
            .build();
        dispatcher.schedule(job);
    }
}
