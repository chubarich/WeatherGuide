package com.example.julia.weatherguide.data.repositories.weather;

import com.example.julia.weatherguide.data.entities.repository.Location;
import com.example.julia.weatherguide.data.entities.repository.WeatherDataModel;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface WeatherRepository {

    Single<WeatherDataModel> getWeather();

    Single<WeatherDataModel> getFreshWeather();

    Completable saveCurrentLocation(Location location, String cityName);

    boolean isLocationInitialized();

}
