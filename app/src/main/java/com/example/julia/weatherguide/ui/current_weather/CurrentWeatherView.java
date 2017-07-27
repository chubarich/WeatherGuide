package com.example.julia.weatherguide.ui.current_weather;

import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.ui.base.view.BaseView;

public interface CurrentWeatherView extends BaseView {

    void showLoading();

    void hideLoading();

    void showNoInternet();

    void showCityNotPicked();

    void showEmptyView();

    void showData(WeatherDataModel data);
}
