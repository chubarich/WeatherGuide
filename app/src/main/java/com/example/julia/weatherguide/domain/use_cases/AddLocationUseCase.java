package com.example.julia.weatherguide.domain.use_cases;


import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.domain.use_cases.base.CompletableUseCase;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.Completable;
import io.reactivex.Scheduler;

public class AddLocationUseCase extends CompletableUseCase<Location> {

    private final LocationRepository locationRepository;

    public AddLocationUseCase(Scheduler worker, Scheduler postExecution, LocationRepository locationRepository) {
        super(worker, postExecution);
        Preconditions.nonNull(locationRepository);
        this.locationRepository = locationRepository;
    }

    @Override
    protected Completable getUseCaseCompletable(Location location) {
        return locationRepository.addLocation(location);
    }
}
