package com.example.julia.weatherguide.interactors;

import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.data.Location;

import io.reactivex.Completable;
import io.reactivex.Scheduler;

public class MainViewInteractorImpl implements MainViewInteractor {

    private CurrentWeatherRepository repository;
    private final Scheduler workerScheduler;
    private final Scheduler uiScheduler;

    public MainViewInteractorImpl(CurrentWeatherRepository currentWeatherRepository,
                                  Scheduler workerScheduler, Scheduler uiScheduler) {
        this.repository = currentWeatherRepository;
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
