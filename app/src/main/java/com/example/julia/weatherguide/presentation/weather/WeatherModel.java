package com.example.julia.weatherguide.presentation.weather;


import android.content.res.Resources;

import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;

public interface WeatherModel {

    void setWeather(Weather weather);

    void bindResources(Resources resources);

    void unbindResources();

}
