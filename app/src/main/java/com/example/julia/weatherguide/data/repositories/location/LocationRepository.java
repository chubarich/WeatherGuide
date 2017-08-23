package com.example.julia.weatherguide.data.repositories.location;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.utils.Optional;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public interface LocationRepository {

    Single<List<LocationPrediction>> getPredictionsForPhrase(String phrase);

    Single<Location> getLocation(LocationPrediction locationPrediction);


    Observable<List<LocationWithId>> subscribeOnLocationsChanges();

    Observable<Optional<LocationWithId>> subscribeOnCurrentLocationChanges();

    Completable addLocationAndSetAsCurrent(Location location);

    Single<LocationWithId> deleteLocation(Location location);

}
