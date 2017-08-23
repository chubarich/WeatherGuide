package com.example.julia.weatherguide.domain.use_cases;

import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.domain.use_cases.base.SingleUseCase;
import com.example.julia.weatherguide.utils.Preconditions;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;


public class GetLocationPredictionsUseCase extends SingleUseCase<List<LocationPrediction>, String> {

    private final LocationRepository locationRepository;

    public GetLocationPredictionsUseCase(Scheduler worker, Scheduler afterExecution,
                                         LocationRepository locationRepository) {
        super(worker, afterExecution);
        Preconditions.nonNull(locationRepository);
        this.locationRepository = locationRepository;
    }

    @Override
    protected Single<List<LocationPrediction>> getUseCaseSingle(String phrase) {
        return locationRepository.getPredictionsForPhrase(phrase);
    }
}
