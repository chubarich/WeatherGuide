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
import com.example.julia.weatherguide.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


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
    public Completable addLocation(Location location) {
        return localService.addLocation(converter.toDatabase(location));
    }

    @Override
    public Completable changeLocationName(Location location, String newName) {
        return localService.addLocation(converter.toDatabase(location, newName));
    }

    @Override
    public Completable deleteLocation(Location location) {
        return localService.deleteLocation(converter.toDatabase(location));
    }

}
