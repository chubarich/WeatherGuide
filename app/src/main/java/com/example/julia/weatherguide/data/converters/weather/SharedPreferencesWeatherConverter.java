package com.example.julia.weatherguide.data.converters.weather;


import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeather;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPrediction;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkCurrentWeather;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkWeatherPrediction;
import com.example.julia.weatherguide.data.entities.presentation.weather.CurrentWeather;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;

import java.util.List;

public class SharedPreferencesWeatherConverter implements WeatherConverter {

    @Override
    public Weather fromNetwork(NetworkCurrentWeather weather, List<NetworkWeatherPrediction> predictions) {


        return null;
    }

    @Override
    public Weather fromDatabase(DatabaseCurrentWeather weather, List<DatabaseWeatherPrediction> predictions) {
        return null;
    }

    @Override
    public DatabaseCurrentWeather fromNetwork(NetworkCurrentWeather weather) {
        return null;
    }

    @Override
    public List<DatabaseWeatherPrediction> fromNetwork(List<NetworkWeatherPrediction> predictions) {
        return null;
    }

}
