package com.example.julia.weatherguide.data.repositories.location;

import com.example.julia.weatherguide.data.converters.location.LocationConverter;
import com.example.julia.weatherguide.data.data_services.location.LocalLocationService;
import com.example.julia.weatherguide.data.data_services.location.NetworkLocationService;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPrediction;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationCoordinates;
import com.example.julia.weatherguide.data.entities.repository.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.EMPTY_DATABASE;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.VALUE_ALREADY_EXISTS;


public class GoogleMapsRepository implements LocationRepository {

    private final LocationConverter converter;
    private final LocalLocationService localService;
    private final NetworkLocationService networkService;


    public GoogleMapsRepository(LocationConverter converter,
                                LocalLocationService localService,
                                NetworkLocationService networkService) {
        Preconditions.nonNull(converter, localService, networkService);
        this.converter = converter;
        this.localService = localService;
        this.networkService = networkService;
    }


    @Override
    public Single<List<LocationWithId>> getLocations() {
        return localService.getLocations().map(list -> {
            List<LocationWithId> result = new ArrayList<>();
            for (DatabaseLocation databaseLocation : list) {
                LocationWithId location = converter.fromDatabase(databaseLocation);
                if (location != null) {
                    result.add(location);
                }
            }
            return result;
        });
    }

    @Override
    public Single<Location> getLocation(LocationPrediction prediction) {
        return networkService.getLocationCoordinates(converter.toNetwork(prediction))
            .doOnSuccess(coordinates -> localService.addLocation(converter.toDatabase(coordinates, prediction)))
            .map(coordinates -> converter.fromNetwork(coordinates, prediction));
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
    public Completable addLocationAndSetAsCurrent(Location location) {
        return localService.getLocation(converter.toDatabase(location))
            .map(DatabaseLocation::getId)
            .onErrorResumeNext(error -> {
                    if (error instanceof ExceptionBundle && ((ExceptionBundle) error).getReason() == EMPTY_DATABASE) {
                        return localService.addLocation(converter.toDatabase(location));
                    } else {
                        return Single.error(error);
                    }
                }
            )
            .doOnSuccess(converter::setCurrentLocationId)
            .toCompletable();
    }

    @Override
    public Completable deleteLocation(Location location) {
        return localService.deleteLocation(converter.toDatabase(location));
    }

}
