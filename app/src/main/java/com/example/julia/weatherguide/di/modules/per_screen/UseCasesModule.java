package com.example.julia.weatherguide.di.modules.per_screen;

import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.di.qualifiers.PostExecutionScheduler;
import com.example.julia.weatherguide.di.qualifiers.WorkerScheduler;
import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.domain.use_cases.AddLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.DeleteLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.GetLocationFromPredictionUseCase;
import com.example.julia.weatherguide.domain.use_cases.GetLocationPredictionsUseCase;
import com.example.julia.weatherguide.domain.use_cases.GetLocationsAndSubscribeUseCase;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;


@Module
public class UseCasesModule {

    @Provides
    @PerScreen
    GetLocationsAndSubscribeUseCase provideGetLocations(@WorkerScheduler Scheduler worker,
                                                        @PostExecutionScheduler Scheduler postExecution,
                                                        LocationRepository locationRepository,
                                                        WeatherRepository weatherRepository) {
        return new GetLocationsAndSubscribeUseCase(worker, postExecution, locationRepository,
            weatherRepository);
    }

    @Provides
    @PerScreen
    DeleteLocationUseCase provideDeleteLocation(@WorkerScheduler Scheduler worker,
                                                @PostExecutionScheduler Scheduler postExecution,
                                                LocationRepository locationRepository) {
        return new DeleteLocationUseCase(worker, postExecution, locationRepository);
    }

    @Provides
    @PerScreen
    AddLocationUseCase provideAddLocationUseCase(@WorkerScheduler Scheduler worker,
                                                 @PostExecutionScheduler Scheduler postExecution,
                                                 LocationRepository locationRepository) {
        return new AddLocationUseCase(worker, postExecution, locationRepository);
    }

    @Provides
    @PerScreen
    GetLocationPredictionsUseCase provideGetLocationPredictionsUseCase(
        @WorkerScheduler Scheduler worker,
        @PostExecutionScheduler Scheduler postExecution,
        LocationRepository locationRepository
    ) {
        return new GetLocationPredictionsUseCase(worker, postExecution, locationRepository);
    }

    @Provides
    @PerScreen
    GetLocationFromPredictionUseCase provideGetLocationFromPredictionUseCase(
        @WorkerScheduler Scheduler worker,
        @PostExecutionScheduler Scheduler postExecution,
        LocationRepository locationRepository
    ) {
        return new GetLocationFromPredictionUseCase(worker, postExecution, locationRepository);
    }

}
