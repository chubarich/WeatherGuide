package com.example.julia.weatherguide.data.data_services;

import com.example.julia.weatherguide.data.data_services.location.LocalLocationService;
import com.example.julia.weatherguide.data.data_services.location.StorIOLocationService;
import com.example.julia.weatherguide.data.database.StorIOFactory;
import com.example.julia.weatherguide.data.database.StorIOWeatherHelper;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import io.reactivex.functions.Predicate;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.NOT_DELETED;


@RunWith(RobolectricTestRunner.class)
public class LocalLocationServiceTest {

    private static final DatabaseLocation FIRST = new DatabaseLocation(5.24, 3.1, "Moscow");
    private static final DatabaseLocation SECOND = new DatabaseLocation(2, 4.21, "London");
    private LocalLocationService localLocationService;

    @Before
    public void before() {
        StorIOSQLite storIOSQLite = StorIOFactory.create(
                new StorIOWeatherHelper(RuntimeEnvironment.application, "database", 1)
        );
        localLocationService = new StorIOLocationService(storIOSQLite);
    }

    @Test
    public void addLocation_returnsInsertedIndex() {
        localLocationService.addLocation(FIRST)
                .test()
                .assertNoErrors()
                .assertValue(1L);

        localLocationService.addLocation(SECOND)
                .test()
                .assertNoErrors()
                .assertValue(2L);
    }

    @Test
    public void deleteLocation_deletesSuccessfully() {
        localLocationService.addLocation(FIRST)
                .test()
                .assertNoErrors()
                .assertValue(1L);

        localLocationService.deleteLocation(FIRST)
                .test()
                .assertNoErrors()
                .assertComplete();
    }

    @Test
    public void deleteLocation_deleteException() {
        localLocationService.deleteLocation(FIRST)
                .test()
                .assertError(error -> {
                    return ((ExceptionBundle) error).getReason() == NOT_DELETED;
                });
    }

    @Test
    public void getLocation_returnValue() {
        localLocationService.addLocation(FIRST)
                .test()
                .assertNoErrors()
                .assertValue(1L);
        Predicate<DatabaseLocation> predicate = databaseLocation -> {
            return databaseLocation.getLatitude() == FIRST.getLatitude()
                    && databaseLocation.getLongitude() == FIRST.getLongitude()
                    && databaseLocation.getName().equals(FIRST.getName())
                    && databaseLocation.getId() == 1;
        };

        localLocationService.getLocation(1L)
                .test()
                .assertNoErrors()
                .assertValue(predicate);
        localLocationService.getLocation(FIRST.getLongitude(), FIRST.getLatitude())
                .test()
                .assertNoErrors()
                .assertValue(predicate);
    }

    @Test
    public void subscribeOnLocationsChanges_returnsEmptyListDefault() {
        localLocationService.subscribeOnLocationsChanges()
                .test()
                .assertNoErrors()
                .assertValue(list -> list.size() == 0);
    }

}
