package com.example.julia.weatherguide.presentation.current_weather;

import com.example.julia.weatherguide.data.entities.presentation.weather.CurrentWeather;
import com.example.julia.weatherguide.presentation.base.view.BaseView;

public interface CurrentWeatherView extends BaseView {

    void showLoading();

    void hideLoading();

    void showNoInternet();

    void showCityNotPicked();

    void showEmptyView();

    void showData(CurrentWeather data);
}
