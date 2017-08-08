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

    Single<List<LocationPrediction>> getPredictionsForPhrase(String phrase);

    Single<Location> getLocation(LocationPrediction locationPrediction);


    Single<List<LocationWithId>> getLocations();

    Completable addLocationAndSetAsCurrent(Location location);

    Completable deleteLocation(Location location);

}
