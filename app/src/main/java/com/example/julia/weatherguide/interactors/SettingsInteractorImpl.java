package com.example.julia.weatherguide.interactors;

import android.content.Context;

import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.repositories.SettingsRepository;
import com.example.julia.weatherguide.services.refresh.CurrentWeatherRefreshDataService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class SettingsInteractorImpl implements SettingsInteractor {

  private static final String JOB_TAG = "refresh_service";
  private static final int WINDOW_IN_MINUTES = 30;

  private Context context;
  private SettingsRepository repository;

  public SettingsInteractorImpl(Context context, SettingsRepository settingsRepository) {
    this.context = context.getApplicationContext();
    this.repository = settingsRepository;
  }

  @Override
  public void destroy() {
    this.context = null;
    this.repository = null;
  }

  @Override
  public void saveRefreshPeriod(long period) {
    repository.saveRefreshInterval(period);
    scheduleForUpdateCurrentWeather((int) period);
  }

  @Override
  public void scheduleForUpdateCurrentWeather(int interval) {
    Driver driver = new GooglePlayDriver(context);
    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
    Job job = dispatcher.newJobBuilder()
        .setService(CurrentWeatherRefreshDataService.class)
        .setTag(JOB_TAG)
        .setLifetime(Lifetime.FOREVER)
        .setRecurring(true)
        .setConstraints(Constraint.ON_ANY_NETWORK)
        .setTrigger(Trigger.executionWindow((int) TimeUnit.MINUTES.toSeconds(interval),
            (int) TimeUnit.MINUTES.toSeconds(interval + WINDOW_IN_MINUTES)))
        .setReplaceCurrent(true)
        .build();
    dispatcher.schedule(job);
  }
}
