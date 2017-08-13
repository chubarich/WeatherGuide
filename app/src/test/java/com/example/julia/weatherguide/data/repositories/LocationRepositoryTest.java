package com.example.julia.weatherguide.data.repositories;

import android.graphics.Path;

import com.example.julia.weatherguide.data.converters.location.LocationConverter;
import com.example.julia.weatherguide.data.converters.location.LocationConverterPlain;
import com.example.julia.weatherguide.data.data_services.location.LocalLocationService;
import com.example.julia.weatherguide.data.data_services.location.NetworkLocationService;
import com.example.julia.weatherguide.data.data_services.settings.SettingsService;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocation;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationCoordinates;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationGeometry;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationResult;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPrediction;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.repositories.location.GoogleMapsRepository;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.utils.Optional;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.CURRENT_LOCATION_DELETION;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.EMPTY_DATABASE;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class LocationRepositoryTest {

    private static final double EPSILON = 0;
    private static final double LATITUDE = 5.2241;
    private static final double LONGITUDE = 2.3124;
    private static final String MAIN_TEXT = "Moscow";

    private LocationRepository locationRepository;
    private SettingsService settingsService;
    private LocationConverter locationConverter;
    private LocalLocationService localService;
    private NetworkLocationService networkService;

    @Before
    public void before() {
        settingsService = mock(SettingsService.class);
        locationConverter = new LocationConverterPlain();
        localService = mock(LocalLocationService.class);
        networkService = mock(NetworkLocationService.class);

        locationRepository = new GoogleMapsRepository(settingsService, locationConverter,
                localService, networkService);
    }

    @Test
    public void getPredictionsForPhrase_throwsTheSameExceptionAsNetworkService() {
        when(networkService.getPredictionsForPhrase(anyString()))
                .thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));

        locationRepository.getPredictionsForPhrase("phrase")
                .test()
                .assertError(error -> ((ExceptionBundle) error).getReason() == NETWORK_UNAVAILABLE);
    }

    @Test
    public void getLocation_returnsConsistentAndSavesLocationAndId() {
        NetworkLocation networkLocation = new NetworkLocation(LATITUDE, LONGITUDE);
        NetworkLocationGeometry geometry = new NetworkLocationGeometry(networkLocation);
        NetworkLocationResult result = new NetworkLocationResult(geometry);
        NetworkLocationCoordinates coordinates = new NetworkLocationCoordinates(result);
        Long index = 1L;

        when(localService.getLocation(anyFloat(), anyFloat()))
                .thenReturn(Single.error(new ExceptionBundle(EMPTY_DATABASE)));
        when(localService.addLocation(any(DatabaseLocation.class)))
                .thenReturn(Single.just(index));
        when(networkService.getLocationCoordinates(any(NetworkLocationPrediction.class)))
                .thenReturn(Single.just(coordinates));


        locationRepository.getLocation(new LocationPrediction("", MAIN_TEXT, ""))
                .test()
                .assertValue(location -> {
                    return location.latitude == LATITUDE
                            && location.longitude == LONGITUDE
                            && location.name.equals(MAIN_TEXT);
                });

        verify(localService, atLeastOnce()).addLocation(any(DatabaseLocation.class));
        verify(settingsService, atLeastOnce()).setCurrentLocationId(index);
    }

    @Test
    public void deleteLocation_throwsExceptionWhenDeletingCurrent() {
        long index = 1L;
        DatabaseLocation databaseLocation = new DatabaseLocation(index, LONGITUDE, LATITUDE, MAIN_TEXT);

        when(localService.getLocation(LONGITUDE, LATITUDE))
                .thenReturn(Single.just(databaseLocation));
        when(settingsService.getCurrentLocationId()).thenReturn(index);


        locationRepository.deleteLocation(new Location(LONGITUDE, LATITUDE, MAIN_TEXT))
                .test()
                .assertError(error -> ((ExceptionBundle) error).getReason() == CURRENT_LOCATION_DELETION);
    }

    @Test
    public void subscribeOnCurrentLocationIdChanges_returnEmptyWhenSourceIsEmpty() {
        when(settingsService.subscribeOnCurrentLocationIdChanges())
                .thenReturn(Observable.just(Optional.of(null)));

        locationRepository.subscribeOnCurrentLocationChanges()
                .test()
                .assertValue(optional -> !optional.isPresent());
    }

    @Test
    public void subscribeOnLocationsChanges_returnValue() {
        DatabaseLocation first = new DatabaseLocation(1L, 45.2, 34.2, "Moscow");
        DatabaseLocation second = new DatabaseLocation(2L, 42.1, 32.1, "London");
        List<DatabaseLocation> locations = new ArrayList<>();
        locations.add(first);
        locations.add(second);

        when(settingsService.subscribeOnCurrentLocationIdChanges())
                .thenReturn(Observable.just(Optional.of(1L)));
        when(localService.subscribeOnLocationsChanges())
                .thenReturn(Observable.just(locations));

        locationRepository.subscribeOnLocationsChanges()
                .test()
                .assertValue(list -> {
                    if (list.size() != 2) return false;
                    LocationWithId firstClone = list.get(0);
                    LocationWithId secondClone = list.get(1);

                    return firstClone.location.name.equals(first.getName())
                            && firstClone.location.longitude == first.getLongitude()
                            && firstClone.location.latitude == first.getLatitude()
                            && firstClone.id == first.getId()
                            && firstClone.location.isCurrent
                            && secondClone.location.name.equals(second.getName())
                            && secondClone.location.longitude == second.getLongitude()
                            && secondClone.location.latitude == second.getLatitude()
                            && secondClone.id == second.getId();
                });


    }

}
