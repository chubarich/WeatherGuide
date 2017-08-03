package com.example.julia.weatherguide.interactors;

import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.data.entities.repository.Location;

import io.reactivex.Completable;
import io.reactivex.Scheduler;

public class MainViewInteractorImpl implements MainViewInteractor {

    private WeatherRepository repository;
    private final Scheduler workerScheduler;
    private final Scheduler uiScheduler;

    public MainViewInteractorImpl(WeatherRepository weatherRepository,
                                  Scheduler workerScheduler, Scheduler uiScheduler) {
        this.repository = weatherRepository;
        this.workerScheduler = workerScheduler;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public Completable saveLocation(float longitude, float latitude, String cityName) {
        return repository.saveCurrentLocation(new Location(longitude, latitude), cityName)
            .subscribeOn(workerScheduler)
            .observeOn(uiScheduler);
    }

}
