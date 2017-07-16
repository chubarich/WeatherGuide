package com.example.julia.weatherguide.interactors;

import com.example.julia.weatherguide.repositories.SettingsRepository;

/**
 * Created by julia on 16.07.17.
 */

public class SettingsInteractorImpl implements SettingsInteractor {
    private final SettingsRepository repository;
    private final CurrentWeatherInteractor schedulerInteractor;

    public SettingsInteractorImpl(SettingsRepository repo, CurrentWeatherInteractor schedulerInteractor) {
        this.repository = repo;
        this.schedulerInteractor = schedulerInteractor;
    }

    @Override
    public void saveRefreshPeriod(long period) {
        repository.saveRefreshInterval(period);
        schedulerInteractor.scheduleForUpdateCurrentWeather((int)period);
    }
}
