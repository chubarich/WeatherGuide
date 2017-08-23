package com.example.julia.weatherguide.presentation.weather;

import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.presentation.base.view.BaseView;


public interface WeatherView extends BaseView {

    boolean isInitialized();

    LocationWithId getLocation();

    void showWeather(Weather weather);

    void showLoading();

    void hideLoading();

    void showNoInternet();

    void showApiError();

}
