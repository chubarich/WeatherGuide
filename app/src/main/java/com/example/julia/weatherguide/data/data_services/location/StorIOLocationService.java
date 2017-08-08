package com.example.julia.weatherguide.data.data_services.location;

import com.example.julia.weatherguide.data.contracts.local.location.LocationContract;
import com.example.julia.weatherguide.data.data_services.base.BaseDatabaseService;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.repository.weather.WeatherNotification;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


public class StorIOLocationService extends BaseDatabaseService implements LocalLocationService {

    private final Subject<List<DatabaseLocation>> locationChangesSubject;

    public StorIOLocationService(StorIOSQLite storIOSQLite) {
        super(storIOSQLite);
        locationChangesSubject = BehaviorSubject.<List<DatabaseLocation>>create()
            .toSerialized();
    }

    @Override
    public Observable<List<DatabaseLocation>> subscribeOnLocationsChanges() {
        locationChangesSubject.onNext(getLocations());
        return locationChangesSubject;
    }

    @Override
    public Single<Long> addLocation(DatabaseLocation location) {
        return Single.fromCallable(() -> {
                Long insertedId = getSQLite().put()
                    .object(location)
                    .prepare()
                    .executeAsBlocking()
                    .insertedId();

                if (insertedId == null) {
                    throw new ExceptionBundle(ExceptionBundle.Reason.NOT_ADDED);
                } else {
                    locationChangesSubject.onNext(getLocations());
                    return insertedId;
                }
            }
        );
    }

    @Override
    public Completable deleteLocation(DatabaseLocation location) {
        return Completable.fromAction(() -> {
                DeleteResult result = getSQLite()
                    .delete()
                    .byQuery(DeleteQuery.builder()
                        .table(LocationContract.TABLE_NAME)
                        .where(LocationContract.COLUMN_NAME_LATITUDE + " = ?"
                            + " AND " + LocationContract.COLUMN_NAME_LONGITUDE + " = ?")
                        .whereArgs(location.getLatitude(), location.getLongitude())
                        .build())
                    .prepare()
                    .executeAsBlocking();

                if (result.numberOfRowsDeleted() == 0) {
                    throw new ExceptionBundle(ExceptionBundle.Reason.NOT_DELETED);
                } else {
                    locationChangesSubject.onNext(getLocations());
                }
            }
        );
    }

    @Override
    public Single<DatabaseLocation> getLocation(DatabaseLocation location) {
        DatabaseLocation databaseLocation = getSQLite()
            .get()
            .object(DatabaseLocation.class)
            .withQuery(Query.builder()
                .table(LocationContract.TABLE_NAME)
                .where(LocationContract.COLUMN_NAME_LATITUDE + " = ?"
                    + " AND " + LocationContract.COLUMN_NAME_LONGITUDE + " = ?")
                .whereArgs((double) location.getLatitude(), (double) location.getLongitude())
                .build())
            .prepare()
            .executeAsBlocking();

        if (databaseLocation == null) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.EMPTY_DATABASE));
        } else {
            return Single.just(databaseLocation);
        }
    }

    // ------------------------------------------ private -----------------------------------------

    private List<DatabaseLocation> getLocations() {
        return getSQLite().get()
            .listOfObjects(DatabaseLocation.class)
            .withQuery(Query.builder()
                .table(LocationContract.TABLE_NAME)
                .build()
            )
            .prepare()
            .executeAsBlocking();
    }
}
