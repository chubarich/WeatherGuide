package com.example.julia.weatherguide.data.data_services.weather;

import com.example.julia.weatherguide.data.contracts.local.weather.CurrentWeatherContract;
import com.example.julia.weatherguide.data.contracts.local.weather.WeatherPredictionContract;
import com.example.julia.weatherguide.data.data_services.base.BaseDatabaseService;
import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeather;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPrediction;
import com.example.julia.weatherguide.data.entities.repository.weather.WeatherNotification;
import com.example.julia.weatherguide.utils.Optional;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


public class StorIOWeatherService extends BaseDatabaseService implements LocalWeatherService {

    private final Subject<WeatherNotification> currentWeatherChangeSubject;

    public StorIOWeatherService(StorIOSQLite storIOSQLite) {
        super(storIOSQLite);
        currentWeatherChangeSubject = BehaviorSubject.createDefault(new WeatherNotification())
            .toSerialized();
    }

    // ----------------------------------------- get ----------------------------------------------

    @Override
    public Single<Optional<DatabaseCurrentWeather>> getCurrentWeather(long locationId) {
        DatabaseCurrentWeather weather = getSQLite().get()
            .object(DatabaseCurrentWeather.class)
            .withQuery(Query.builder()
                .table(CurrentWeatherContract.TABLE_NAME)
                .where(CurrentWeatherContract.COLUMN_NAME_LOCATION_ID + " = ?")
                .whereArgs(locationId)
                .build())
            .prepare()
            .executeAsBlocking();

        return Single.just(Optional.of(weather));
    }

    @Override
    public Single<List<DatabaseWeatherPrediction>> getPredictions(long locationId, List<String> dates) {
        List<DatabaseWeatherPrediction> result = new ArrayList<>();

        for (int i = 0; i < dates.size(); ++i) {
            DatabaseWeatherPrediction weather = getSQLite().get()
                .object(DatabaseWeatherPrediction.class)
                .withQuery(Query.builder()
                    .table(WeatherPredictionContract.TABLE_NAME)
                    .where(WeatherPredictionContract.COLUMN_NAME_DATE + " = ?" +
                        " AND " + CurrentWeatherContract.COLUMN_NAME_LOCATION_ID + " = ?")
                    .whereArgs(dates.get(i), locationId)
                    .build())
                .prepare()
                .executeAsBlocking();

            result.add(weather);
        }

        return Single.just(result);
    }

    @Override
    public Observable<WeatherNotification> subscribeOnCurrentWeatherChanges() {
        return currentWeatherChangeSubject;
    }

    // ----------------------------------------- put ----------------------------------------------

    @Override
    public Completable saveCurrentWeather(DatabaseCurrentWeather weather) {
        return Completable.fromAction(() -> {
                getSQLite().put()
                    .object(weather)
                    .prepare()
                    .executeAsBlocking();

                currentWeatherChangeSubject.onNext(new WeatherNotification());
            }
        );
    }

    @Override
    public Completable savePredictions(List<DatabaseWeatherPrediction> predictions) {
        return Completable.fromAction(() -> {
                for (DatabaseWeatherPrediction prediction : predictions) {
                    getSQLite().put()
                        .object(prediction)
                        .prepare()
                        .executeAsBlocking();
                }
            }
        );
    }

    // ---------------------------------------- delete --------------------------------------------

    @Override
    public Completable deleteWeather(long locationId) {
        return Completable.fromAction(() -> {
                getSQLite().delete()
                    .byQuery(DeleteQuery.builder()
                        .table(CurrentWeatherContract.TABLE_NAME)
                        .where(CurrentWeatherContract.COLUMN_NAME_LOCATION_ID + " = ?")
                        .whereArgs(locationId)
                        .build())
                    .prepare().executeAsBlocking();

                getSQLite().delete()
                    .byQuery(DeleteQuery.builder()
                        .table(WeatherPredictionContract.TABLE_NAME)
                        .where(WeatherPredictionContract.COLUMN_NAME_LOCATION_ID + " = ?")
                        .whereArgs(locationId)
                        .build())
                    .prepare().executeAsBlocking();
            }
        );
    }

}
