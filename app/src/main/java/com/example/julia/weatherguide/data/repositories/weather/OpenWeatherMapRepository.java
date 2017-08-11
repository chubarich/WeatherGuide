package com.example.julia.weatherguide.data.repositories.weather;

import android.support.annotation.IntRange;

import com.example.julia.weatherguide.data.converters.weather.WeatherConverter;
import com.example.julia.weatherguide.data.data_services.weather.LocalWeatherService;
import com.example.julia.weatherguide.data.data_services.weather.NetworkWeatherService;
import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeather;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.data.entities.repository.weather.WeatherNotification;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.helpers.DatetimeHelper;
import com.example.julia.weatherguide.utils.Optional;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.API_ERROR;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.EMPTY_DATABASE;
import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy.FROM_ANYWHERE;
import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy.FROM_CACHE;


public class OpenWeatherMapRepository implements WeatherRepository {

    private static final int PREDICTION_DAYS_COUNT = 16;

    private final DatetimeHelper datetimeHelper;
    private final WeatherConverter converter;
    private final LocalWeatherService localService;
    private final NetworkWeatherService networkService;

    public OpenWeatherMapRepository(DatetimeHelper datetimeHelper,
                                    WeatherConverter converter,
                                    LocalWeatherService localService,
                                    NetworkWeatherService networkService) {
        Preconditions.nonNull(datetimeHelper, converter, localService, networkService);
        this.datetimeHelper = datetimeHelper;
        this.converter = converter;
        this.localService = localService;
        this.networkService = networkService;
    }

    // ----------------------------------- WeatherRepository --------------------------------------

    @Override
    public Single<Weather> getWeather(LocationWithId locationWithId, GetWeatherStrategy getWeatherStrategy) {
        if (getWeatherStrategy == FROM_CACHE) {
            return getWeatherFromLocal(locationWithId);
        } else {
            return getWeatherFromNetwork(locationWithId)
                .onErrorResumeNext((error) -> {
                    if (getWeatherStrategy == FROM_ANYWHERE) {
                        return getWeatherFromLocal(locationWithId);
                    } else {
                        return Single.error(error);
                    }
                });
        }
    }

    @Override
    public Observable<WeatherNotification> subscribeOnCurrentWeatherChanges() {
        return localService.subscribeOnCurrentWeatherChanges();
    }

    @Override
    public Completable deleteWeather(LocationWithId locationWithId) {
        return localService.deleteWeather(locationWithId.id);
    }

    @Override
    public Integer getTemperature(LocationWithId locationWithId) {
        Optional<DatabaseCurrentWeather> optional = localService.getCurrentWeather(locationWithId.id)
            .blockingGet();

        if (!optional.isPresent()) {
            return null;
        } else {
            return converter.getTemperature(optional.get());
        }
    }

    // -------------------------------------- private ---------------------------------------------

    private Single<Weather> getWeatherFromNetwork(LocationWithId locationWithId) {
        return Single.zip(
            networkService.getCurrentWeather(locationWithId.location.longitude, locationWithId.location.latitude)
                .doOnSuccess(weather -> {
                    localService.saveCurrentWeather(converter.fromNetwork(weather, locationWithId.id))
                        .onErrorComplete()
                        .blockingAwait();
                }),
            networkService.getPredictions(locationWithId.location.longitude, locationWithId.location.latitude, PREDICTION_DAYS_COUNT)
                .doOnSuccess(predictions -> {
                    localService.savePredictions(converter.fromNetwork(predictions, locationWithId.id))
                        .onErrorComplete()
                        .blockingAwait();
                })
                .doOnError(t -> {
                    if (t instanceof NullPointerException) { // we couldn`t parse json -- API_ERROR
                        throw new ExceptionBundle(API_ERROR);
                    } else if (t instanceof Exception) {
                        throw (Exception) t;
                    }
                }),
            converter::fromNetwork
        );

    }

    private Single<Weather> getWeatherFromLocal(LocationWithId locationWithId) {
        return Single.zip(
            localService.getCurrentWeather(locationWithId.id)
                .map(optional -> {
                    if (!optional.isPresent()) throw new ExceptionBundle(EMPTY_DATABASE);
                    return optional.get();
                }),
            localService.getPredictions(
                locationWithId.id,
                datetimeHelper.getNextDates(PREDICTION_DAYS_COUNT)
            ),
            converter::fromDatabase
        );
    }

    public enum GetWeatherStrategy {
        FROM_NETWORK,
        FROM_CACHE,
        FROM_ANYWHERE
    }
}
