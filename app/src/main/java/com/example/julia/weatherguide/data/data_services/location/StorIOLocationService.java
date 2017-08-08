package com.example.julia.weatherguide.data.data_services.location;

import com.example.julia.weatherguide.data.contracts.local.location.LocationContract;
import com.example.julia.weatherguide.data.data_services.base.BaseDatabaseService;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public class StorIOLocationService extends BaseDatabaseService implements LocalLocationService {


    public StorIOLocationService(StorIOSQLite storIOSQLite) {
        super(storIOSQLite);
    }

    @Override
    public Single<List<DatabaseLocation>> getLocations() {
        return Single.just(getSQLite().get()
            .listOfObjects(DatabaseLocation.class)
            .withQuery(Query.builder()
                .table(LocationContract.TABLE_NAME)
                .build()
            )
            .prepare()
            .executeAsBlocking()
        );
    }

    @Override
    public Completable addLocation(DatabaseLocation location) {
        return Completable.fromAction(() ->
            getSQLite().put()
                .object(location)
                .prepare()
                .executeAsBlocking()
        );
    }

    @Override
    public Completable deleteLocation(DatabaseLocation location) {
        return Completable.fromAction(() -> {
                DeleteResult result = getSQLite().delete()
                    .byQuery(DeleteQuery.builder()
                        .table(LocationContract.TABLE_NAME)
                        .where(LocationContract.COLUMN_NAME_NAME + " = ?")
                        .whereArgs(location.getName())
                        .build())
                    .prepare()
                    .executeAsBlocking();

                if (result.numberOfRowsDeleted() == 0) {
                    throw new ExceptionBundle(ExceptionBundle.Reason.NOT_DELETED);
                }
            }
        );
    }

    @Override
    public Long getLocationId(DatabaseLocation location) {
        DatabaseLocation databaseLocation = getSQLite()
            .get()
            .object(DatabaseLocation.class)
            .withQuery(Query.builder()
                .table(LocationContract.TABLE_NAME)
                .where(LocationContract.COLUMN_NAME_LATITUDE + " = ?"
                    + " AND " + LocationContract.COLUMN_NAME_LONGITUDE + " = ?")
                .whereArgs(location.getLatitude(), location.getLongitude())
                .build())
            .prepare()
            .executeAsBlocking();

        if (databaseLocation == null) {
            return null;
        } else {
            return databaseLocation.getId();
        }
    }
}
