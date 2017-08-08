package com.example.julia.weatherguide.data.converters.weather;

import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeather;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPrediction;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkCurrentWeather;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkWeatherPrediction;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;

import java.util.List;


public interface WeatherConverter {

    Weather fromNetwork(NetworkCurrentWeather weather, List<NetworkWeatherPrediction> predictions);

    Weather fromDatabase(DatabaseCurrentWeather weather, List<DatabaseWeatherPrediction> predictions);

    DatabaseCurrentWeather fromNetwork(NetworkCurrentWeather weather);

    List<DatabaseWeatherPrediction> fromNetwork(List<NetworkWeatherPrediction> predictions);

    Double mainTemperatureFromDatabase(DatabaseCurrentWeather databaseCurrentWeather);
}
