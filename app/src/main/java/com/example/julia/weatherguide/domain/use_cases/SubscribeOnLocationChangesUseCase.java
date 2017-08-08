package com.example.julia.weatherguide.domain.use_cases;

import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;
import com.example.julia.weatherguide.data.entities.repository.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.repository.weather.WeatherNotification;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.domain.use_cases.base.ObservableUseCase;
import com.example.julia.weatherguide.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;


public class SubscribeOnLocationChangesUseCase extends ObservableUseCase<List<LocationWithTemperature>, Void> {

    private final LocationRepository locationRepository;
    private final WeatherRepository weatherRepository;

    public SubscribeOnLocationChangesUseCase(Scheduler worker, Scheduler postExecution,
                                             LocationRepository locationRepository,
                                             WeatherRepository weatherRepository) {
        super(worker, postExecution);
        Preconditions.nonNull(locationRepository, weatherRepository);
        this.locationRepository = locationRepository;
        this.weatherRepository = weatherRepository;
    }

    @Override
    protected Observable<List<LocationWithTemperature>> getUseCaseObservable(Void aVoid) {
        return Observable.combineLatest(
            locationRepository.subscribeOnLocationsChanges(),
            weatherRepository.subscribeOnCurrentWeatherChanges(),
            (List<LocationWithId> locations, WeatherNotification notification) -> {
                List<LocationWithTemperature> result = new ArrayList<>();
                for (LocationWithId location : locations) {
                    Double temperature = weatherRepository.getTemperature(location);
                    result.add(new LocationWithTemperature(location.location, temperature));
                }
                return result;
            });
    }
}
