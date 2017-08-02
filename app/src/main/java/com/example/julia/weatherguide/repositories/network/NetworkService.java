package com.example.julia.weatherguide.repositories.network;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.network.weather_data.WeatherInCity;

import io.reactivex.Single;

public interface NetworkService {

    Single<WeatherInCity> getCurrentWeather(Location location);

}
