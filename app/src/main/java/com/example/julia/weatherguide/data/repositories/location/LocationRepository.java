package com.example.julia.weatherguide.data.repositories.location;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationCoordinates;
import com.example.julia.weatherguide.data.entities.repository.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public interface LocationRepository {

    Single<List<LocationWithId>> getLocations();

    Single<List<LocationPrediction>> getPredictionsForPhrase(String phrase);

    Single<Location> getLocation(LocationPrediction locationPrediction);

    Completable addLocation(Location location);

    Completable changeLocationName(Location location, String newName);

    Completable deleteLocation(Location location);

}
