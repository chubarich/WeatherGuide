package com.example.julia.weatherguide.domain.use_cases;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.domain.use_cases.base.CompletableUseCase;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.Completable;
import io.reactivex.Scheduler;


public class DeleteLocationUseCase extends CompletableUseCase<Location> {

    private final LocationRepository locationRepository;
    private final WeatherRepository weatherRepository;

    public DeleteLocationUseCase(Scheduler worker, Scheduler postExecution,
                                 LocationRepository locationRepository,
                                 WeatherRepository weatherRepository) {
        super(worker, postExecution);
        Preconditions.nonNull(locationRepository);
        this.locationRepository = locationRepository;
        this.weatherRepository = weatherRepository;
    }

    @Override
    protected Completable getUseCaseCompletable(Location location) {
        return locationRepository.deleteLocation(location)
            .flatMapCompletable(locationWithId -> {
                return weatherRepository.deleteWeather(locationWithId)
                    .onErrorComplete();
            });
    }

}
