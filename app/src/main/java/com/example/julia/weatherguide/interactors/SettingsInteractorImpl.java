package com.example.julia.weatherguide.interactors;

import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.repositories.SettingsRepository;

import javax.inject.Inject;

/**
 * Created by julia on 16.07.17.
 */

public class SettingsInteractorImpl implements SettingsInteractor {

    @Inject
    SettingsRepository repository;

    @Inject
    CurrentWeatherInteractor schedulerInteractor;

    public SettingsInteractorImpl() {
        WeatherGuideApplication.getDataComponent().inject(this);
    }

    @Override
    public void saveRefreshPeriod(long period) {
        repository.saveRefreshInterval(period);
        schedulerInteractor.scheduleForUpdateCurrentWeather((int)period);
    }
}
