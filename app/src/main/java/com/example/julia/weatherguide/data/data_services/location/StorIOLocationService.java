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

    private static final double EPSILON = 0.001;

    private final Subject<List<DatabaseLocation>> locationChangesSubject;

    public StorIOLocationService(StorIOSQLite storIOSQLite) {
        super(storIOSQLite);
        locationChangesSubject = BehaviorSubject.createDefault(getLocations())
            .toSerialized();
    }

    @Override
    public Observable<List<DatabaseLocation>> subscribeOnLocationsChanges() {
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
                        .where("(" + LocationContract.COLUMN_NAME_LATITUDE + " BETWEEN ? AND ?)"
                            + " AND (" + LocationContract.COLUMN_NAME_LONGITUDE + " BETWEEN ? AND ?)")
                        .whereArgs(location.getLatitude() - EPSILON, location.getLatitude() + EPSILON,
                            location.getLongitude() - EPSILON, location.getLongitude() + EPSILON)
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
    public Single<DatabaseLocation> getLocation(double longitude, double latitude) {
        DatabaseLocation databaseLocation = getSQLite()
            .get()
            .object(DatabaseLocation.class)
            .withQuery(Query.builder()
                .table(LocationContract.TABLE_NAME)
                .where("(" + LocationContract.COLUMN_NAME_LATITUDE + " BETWEEN ? AND ?)"
                    + " AND (" + LocationContract.COLUMN_NAME_LONGITUDE + " BETWEEN ? AND ?)")
                .whereArgs(latitude - EPSILON, latitude + EPSILON,
                    longitude - EPSILON, longitude + EPSILON)
                .build())
            .prepare()
            .executeAsBlocking();

        if (databaseLocation == null) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.EMPTY_DATABASE));
        } else {
            return Single.just(databaseLocation);
        }
    }

    @Override
    public Single<DatabaseLocation> getLocation(long id) {
        DatabaseLocation databaseLocation = getSQLite()
            .get()
            .object(DatabaseLocation.class)
            .withQuery(Query.builder()
                .table(LocationContract.TABLE_NAME)
                .where(LocationContract.COLUMN_NAME_ID + "  = ?")
                .whereArgs(id)
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
