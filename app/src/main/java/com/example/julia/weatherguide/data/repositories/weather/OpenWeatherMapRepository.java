package com.example.julia.weatherguide.data.repositories.weather;

import android.support.annotation.IntRange;

import com.example.julia.weatherguide.data.converters.weather.WeatherConverter;
import com.example.julia.weatherguide.data.data_services.weather.LocalWeatherService;
import com.example.julia.weatherguide.data.data_services.weather.NetworkWeatherService;
import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeather;
import com.example.julia.weatherguide.data.entities.repository.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.data.entities.repository.weather.WeatherNotification;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.helpers.DatetimeHelper;
import com.example.julia.weatherguide.utils.Optional;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.EMPTY_DATABASE;
import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy.FROM_ANYWHERE;
import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy.FROM_CACHE;


public class OpenWeatherMapRepository implements WeatherRepository {

    @IntRange(from = 1, to = 16)
    // TODO: let user choose
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
            return getWeatherFromLocal(locationWithId.id);
        } else {
            return getWeatherFromNetwork(locationWithId.location.longitude, locationWithId.location.latitude)
                .onErrorResumeNext((error) -> {
                    if (getWeatherStrategy == FROM_ANYWHERE) {
                        return getWeatherFromLocal(locationWithId.id);
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
    public Double getTemperature(LocationWithId locationWithId) {
        Optional<DatabaseCurrentWeather> optional = localService.getCurrentWeather(locationWithId.id)
            .blockingGet();

        if (!optional.isPresent()) {
            return null;
        } else {
            return optional.get().getMainTemperature();
        }
    }

    // -------------------------------------- private ---------------------------------------------

    private Single<Weather> getWeatherFromNetwork(double longitude, double latitude) {
        return Single.zip(
            networkService.getCurrentWeather(longitude, latitude)
                .doOnSuccess(weather ->
                    localService.saveCurrentWeather(converter.fromNetwork(weather))
                        .onErrorComplete()
                        .blockingAwait()
                ),
            networkService.getPredictions(longitude, latitude, PREDICTION_DAYS_COUNT)
                .doOnSuccess(predictions ->
                    localService.savePredictions(converter.fromNetwork(predictions))
                        .onErrorComplete()
                        .blockingAwait()
                ),
            converter::fromNetwork
        );
    }

    private Single<Weather> getWeatherFromLocal(long locationId) {
        return Single.zip(
            localService.getCurrentWeather(locationId)
                .map(optional -> {
                    if (!optional.isPresent()) throw new ExceptionBundle(EMPTY_DATABASE);
                    return optional.get();
                }),
            localService.getPredictions(
                locationId,
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
