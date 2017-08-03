package com.example.julia.weatherguide.interactors;

import com.example.julia.weatherguide.data.entities.repository.WeatherDataModel;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class CurrentWeatherInteractorImpl implements CurrentWeatherInteractor {

    private final WeatherRepository repository;
    private final Scheduler workerScheduler;
    private final Scheduler uiScheduler;

    public CurrentWeatherInteractorImpl(WeatherRepository weatherRepository,
                                        Scheduler workerScheduler,
                                        Scheduler uiScheduler) {
        this.repository = weatherRepository;
        this.workerScheduler = workerScheduler;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public Single<WeatherDataModel> getCurrentWeather() {
        return repository.getWeather()
            .subscribeOn(workerScheduler)
            .observeOn(uiScheduler);
    }

    @Override
    public Single<WeatherDataModel> getFreshCurrentWeather() {
        return repository.getFreshWeather()
            .subscribeOn(workerScheduler)
            .observeOn(uiScheduler);
    }

}
