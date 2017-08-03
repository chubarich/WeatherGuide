package com.example.julia.weatherguide.data.data_services.local.weather;

import com.example.julia.weatherguide.data.entities.repository.WeatherDataModel;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface LocalWeatherService {

    Single<WeatherDataModel> getWeather();

    Completable saveWeatherForCurrentLocation(WeatherDataModel data);

}
