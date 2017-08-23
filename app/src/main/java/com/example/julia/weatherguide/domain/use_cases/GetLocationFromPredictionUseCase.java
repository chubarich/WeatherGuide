package com.example.julia.weatherguide.domain.use_cases;


import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.domain.use_cases.base.SingleUseCase;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.Scheduler;
import io.reactivex.Single;


public class GetLocationFromPredictionUseCase extends SingleUseCase<Location, LocationPrediction> {

    private final LocationRepository locationRepository;

    public GetLocationFromPredictionUseCase(Scheduler worker, Scheduler postExecution,
                                         LocationRepository locationRepository) {
        super(worker, postExecution);
        Preconditions.nonNull(locationRepository);
        this.locationRepository = locationRepository;
    }

    @Override
    protected Single<Location> getUseCaseSingle(LocationPrediction locationPrediction) {
        return locationRepository.getLocation(locationPrediction);
    }
}
