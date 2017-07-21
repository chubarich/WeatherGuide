package com.example.julia.weatherguide.ui.current_weather;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.ui.base.BasePresenter;

/**
 * Created by julia on 21.07.17.
 */

public interface CurrentWeatherPresenter<V extends CurrentWeatherView> extends BasePresenter<V> {

    void loadCurrentWeatherForLocation(@NonNull String location);
    void refreshCurrentWeather(@NonNull String location);
}
