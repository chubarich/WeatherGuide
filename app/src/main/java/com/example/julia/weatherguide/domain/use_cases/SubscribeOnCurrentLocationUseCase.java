package com.example.julia.weatherguide.domain.use_cases;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.domain.use_cases.base.ObservableUseCase;
import com.example.julia.weatherguide.domain.use_cases.base.SingleUseCase;
import com.example.julia.weatherguide.utils.Optional;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;


public class SubscribeOnCurrentLocationUseCase extends ObservableUseCase<Optional<Location>, Void> {

    private final LocationRepository locationRepository;

    public SubscribeOnCurrentLocationUseCase(Scheduler worker, Scheduler postExecution,
                                             LocationRepository locationRepository) {
        super(worker, postExecution);
        this.locationRepository = locationRepository;
    }

    @Override
    protected Observable<Optional<Location>> getUseCaseObservable(Void aVoid) {
        return locationRepository.subscribeOnCurrentLocationChanges();
    }
}
