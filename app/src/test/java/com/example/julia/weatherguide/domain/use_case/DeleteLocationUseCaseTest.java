package com.example.julia.weatherguide.domain.use_case;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.domain.use_cases.AddLocationAndSetAsCurrentUseCase;
import com.example.julia.weatherguide.domain.use_cases.DeleteLocationUseCase;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.EMPTY_DATABASE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DeleteLocationUseCaseTest {

    private static final Location DUMMY = new Location(0, 0, "");
    private static final LocationWithId DUMMY_WITH_ID = new LocationWithId(1, DUMMY);
    private DeleteLocationUseCase useCase;
    private LocationRepository locationRepository;
    private WeatherRepository weatherRepository;
    private TestScheduler scheduler;

    @Before
    public void before() {
        scheduler = new TestScheduler();
        locationRepository = mock(LocationRepository.class);
        weatherRepository = mock(WeatherRepository.class);
        useCase = new DeleteLocationUseCase(scheduler, scheduler, locationRepository, weatherRepository);
    }

    @Test
    public void execute_completesWithRepository() {
        when(locationRepository.deleteLocation(any(Location.class)))
                .thenReturn(Single.just(DUMMY_WITH_ID));
        when(weatherRepository.deleteWeather(any(LocationWithId.class)))
                .thenReturn(Completable.error(new ExceptionBundle(EMPTY_DATABASE)));

        TestObserver<Void> observer = useCase.execute(DUMMY).test();
        scheduler.triggerActions();

        observer.assertNoErrors().assertComplete();
        verify(locationRepository, times(1)).deleteLocation(any(Location.class));
        verify(weatherRepository, times(1)).deleteWeather(any(LocationWithId.class));
    }

    @Test
    public void execute_throwsExceptionWithRepository() {
        when(locationRepository.deleteLocation(any(Location.class)))
                .thenReturn(Single.error(new ExceptionBundle(EMPTY_DATABASE)));
        when(weatherRepository.deleteWeather(any(LocationWithId.class)))
                .thenReturn(Completable.fromAction(() -> {}));

        TestObserver<Void> observer = useCase.execute(DUMMY).test();
        scheduler.triggerActions();

        observer.assertError(error -> ((ExceptionBundle)error).getReason() == EMPTY_DATABASE);
        verify(locationRepository, times(1)).deleteLocation(any(Location.class));
        verify(weatherRepository, times(0)).deleteWeather(any(LocationWithId.class));
    }

}
