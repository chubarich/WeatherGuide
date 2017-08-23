package com.example.julia.weatherguide.domain.use_case;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.domain.use_cases.AddLocationAndSetAsCurrentUseCase;
import com.example.julia.weatherguide.domain.use_cases.DeleteLocationUseCase;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddLocationAndSetAsCurrentUseCaseTest {

    private static final Location DUMMY = new Location(0, 0, "");
    private AddLocationAndSetAsCurrentUseCase useCase;
    private LocationRepository locationRepository;
    private TestScheduler scheduler;

    @Before
    public void before() {
        scheduler = new TestScheduler();
        locationRepository = mock(LocationRepository.class);
        useCase = new AddLocationAndSetAsCurrentUseCase(scheduler, scheduler, locationRepository);
    }

    @Test
    public void execute_completesWithRepository() {
        when(locationRepository.addLocationAndSetAsCurrent(any(Location.class)))
                .thenReturn(Completable.fromAction(() -> {}));

        TestObserver<Void> observer = useCase.execute(DUMMY).test();
        scheduler.triggerActions();

        observer.assertNoErrors().assertComplete();
        verify(locationRepository, times(1)).addLocationAndSetAsCurrent(any(Location.class));
    }

    @Test
    public void execute_throwsExceptionWithRepository() {
        when(locationRepository.addLocationAndSetAsCurrent(any(Location.class)))
                .thenReturn(Completable.error(new NullPointerException())); // any exception

        TestObserver<Void> observer = useCase.execute(DUMMY).test();
        scheduler.triggerActions();

        observer.assertError(error -> error instanceof NullPointerException);
        verify(locationRepository, times(1)).addLocationAndSetAsCurrent(any(Location.class));
    }

}
