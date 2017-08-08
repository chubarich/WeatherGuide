package com.example.julia.weatherguide.data.data_services.weather;

import com.example.julia.weatherguide.data.entities.network.weather.NetworkCurrentWeather;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkWeatherPrediction;

import java.util.List;

import io.reactivex.Single;


public interface NetworkWeatherService {

    Single<NetworkCurrentWeather> getCurrentWeather(float latitude, float longitude);

    Single<List<NetworkWeatherPrediction>> getPredictions(float latitude, float longitude, int count);

}
