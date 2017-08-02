package com.example.julia.weatherguide.repositories;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface CurrentWeatherRepository {

    Single<WeatherDataModel> getCurrentWeather();

    Single<WeatherDataModel> getFreshCurrentWeather();

    Completable saveCurrentLocation(Location location, String cityName);

    boolean isLocationInitialized();

}
