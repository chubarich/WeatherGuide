package com.example.julia.weatherguide.data.data_services.weather;

import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeather;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPrediction;
import com.example.julia.weatherguide.data.entities.repository.weather.WeatherNotification;
import com.example.julia.weatherguide.utils.Optional;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


public interface LocalWeatherService {

    Single<Optional<DatabaseCurrentWeather>> getCurrentWeather(long locationId);

    Single<List<DatabaseWeatherPrediction>> getPredictions(long locationId, List<String> dates);

    Completable saveCurrentWeather(DatabaseCurrentWeather weather);

    Completable savePredictions(List<DatabaseWeatherPrediction> predictions);

    Completable deleteWeather(long locationId);

    Observable<WeatherNotification> subscribeOnCurrentWeatherChanges();

}
