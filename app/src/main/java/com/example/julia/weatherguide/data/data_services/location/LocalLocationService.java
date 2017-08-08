package com.example.julia.weatherguide.data.data_services.location;

import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public interface LocalLocationService {

    Single<List<DatabaseLocation>> getLocations();

    Completable addLocation(DatabaseLocation location);

    Completable deleteLocation(DatabaseLocation location);

    Long getLocationId(DatabaseLocation location);

}
