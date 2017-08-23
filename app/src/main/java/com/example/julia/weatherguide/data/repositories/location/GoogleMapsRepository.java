package com.example.julia.weatherguide.data.repositories.location;

import com.example.julia.weatherguide.data.converters.location.LocationConverter;
import com.example.julia.weatherguide.data.data_services.location.LocalLocationService;
import com.example.julia.weatherguide.data.data_services.location.NetworkLocationService;
import com.example.julia.weatherguide.data.data_services.settings.SettingsService;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPrediction;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.utils.Optional;
import com.example.julia.weatherguide.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.EMPTY_DATABASE;


public class GoogleMapsRepository implements LocationRepository {

    private final SettingsService settingsService;
    private final LocationConverter converter;
    private final LocalLocationService localService;
    private final NetworkLocationService networkService;


    public GoogleMapsRepository(SettingsService settingsService,
                                LocationConverter converter,
                                LocalLocationService localService,
                                NetworkLocationService networkService) {
        Preconditions.nonNull(converter, localService, networkService);
        this.settingsService = settingsService;
        this.converter = converter;
        this.localService = localService;
        this.networkService = networkService;
    }

    @Override
    public Single<Location> getLocation(LocationPrediction prediction) {
        return networkService.getLocationCoordinates(converter.toNetwork(prediction))
            .map(coordinates -> converter.fromNetwork(coordinates, prediction))
            .flatMap(location -> {
                return addLocationAndSetAsCurrent(location)
                    .onErrorComplete()
                    .toSingle(() -> location);
            });
    }

    @Override
    public Single<List<LocationPrediction>> getPredictionsForPhrase(String phrase) {
        return networkService.getPredictionsForPhrase(phrase)
            .map(list -> {
                List<LocationPrediction> result = new ArrayList<>();
                for (NetworkLocationPrediction locationPrediction : list) {
                    result.add(converter.fromNetwork(locationPrediction));
                }
                return result;
            });
    }


    @Override
    public Observable<List<LocationWithId>> subscribeOnLocationsChanges() {
        return Observable.combineLatest(
            localService.subscribeOnLocationsChanges(),
            settingsService.subscribeOnCurrentLocationIdChanges(),
            this::getLocations
        );
    }

    @Override
    public Observable<Optional<LocationWithId>> subscribeOnCurrentLocationChanges() {
        return settingsService.subscribeOnCurrentLocationIdChanges()
            .map(id -> {
                if (!id.isPresent()) {
                    return Optional.of(null);
                } else {
                    return localService.getLocation(id.get())
                        .map(location -> converter.fromDatabase(location, id.get()))
                        .map(Optional::of)
                        .onErrorReturnItem(Optional.of(null))
                        .blockingGet();
                }
            });
    }

    @Override
    public Completable addLocationAndSetAsCurrent(Location location) {
        return localService.getLocation(location.longitude, location.latitude)
            .map(DatabaseLocation::getId)
            .onErrorResumeNext(error -> {
                if (error instanceof ExceptionBundle && ((ExceptionBundle) error).getReason() == EMPTY_DATABASE) {
                    return localService.addLocation(converter.toDatabase(location));
                } else {
                    return Single.error(error);
                }
            })
            .doOnSuccess(settingsService::setCurrentLocationId)
            .toCompletable();
    }

    @Override
    public Single<LocationWithId> deleteLocation(Location location) {
        return localService.getLocation(location.longitude, location.latitude)
            .flatMap(databaseLocation -> {
                if (databaseLocation.getId().equals(settingsService.getCurrentLocationId())) {
                    return Single.error(new ExceptionBundle(ExceptionBundle.Reason.CURRENT_LOCATION_DELETION));
                } else {
                    return localService.deleteLocation(databaseLocation)
                        .toSingle(() -> {
                            return converter.fromDatabase(databaseLocation, settingsService.getCurrentLocationId());
                        });
                }
            });
    }

    // --------------------------------------- private --------------------------------------------

    private List<LocationWithId> getLocations(List<DatabaseLocation> locations, Optional<Long> currentLocationId) {
        List<LocationWithId> result = new ArrayList<>();
        for (DatabaseLocation databaseLocation : locations) {
            LocationWithId location = converter.fromDatabase(databaseLocation, currentLocationId.get());
            if (location != null) {
                result.add(location);
            }
        }
        return result;
    }

}
