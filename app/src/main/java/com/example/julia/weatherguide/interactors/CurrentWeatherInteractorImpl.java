package com.example.julia.weatherguide.interactors;

import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class CurrentWeatherInteractorImpl implements CurrentWeatherInteractor {

    private final CurrentWeatherRepository repository;
    private final Scheduler workerScheduler;
    private final Scheduler uiScheduler;

    public CurrentWeatherInteractorImpl(CurrentWeatherRepository currentWeatherRepository,
                                        Scheduler workerScheduler,
                                        Scheduler uiScheduler) {
        this.repository = currentWeatherRepository;
        this.workerScheduler = workerScheduler;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public Single<WeatherDataModel> getCurrentWeather() {
        return repository.getCurrentWeather()
            .subscribeOn(workerScheduler)
            .observeOn(uiScheduler);
    }

    @Override
    public Single<WeatherDataModel> getFreshCurrentWeather() {
        return repository.getFreshCurrentWeather()
            .subscribeOn(workerScheduler)
            .observeOn(uiScheduler);
    }

}
