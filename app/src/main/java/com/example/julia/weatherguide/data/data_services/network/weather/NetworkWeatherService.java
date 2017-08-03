package com.example.julia.weatherguide.data.data_services.network.weather;

import com.example.julia.weatherguide.data.entities.repository.Location;
import com.example.julia.weatherguide.data.entities.remote.WeatherInCity;

import io.reactivex.Single;


public interface NetworkWeatherService {

    Single<WeatherInCity> getCurrentWeather(Location location);

}
