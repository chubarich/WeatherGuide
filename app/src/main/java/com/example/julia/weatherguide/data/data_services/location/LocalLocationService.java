package com.example.julia.weatherguide.data.data_services.location;

import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public interface LocalLocationService {

    Observable<List<DatabaseLocation>> subscribeOnLocationsChanges();

    Single<Long> addLocation(DatabaseLocation location);

    Completable deleteLocation(DatabaseLocation location);

    Single<DatabaseLocation> getLocation(DatabaseLocation location);

}
