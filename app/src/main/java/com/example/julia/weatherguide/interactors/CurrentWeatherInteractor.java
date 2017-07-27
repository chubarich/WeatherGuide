package com.example.julia.weatherguide.interactors;

import com.example.julia.weatherguide.repositories.data.WeatherDataModel;

import io.reactivex.Single;

public interface CurrentWeatherInteractor {

    Single<WeatherDataModel> getCurrentWeather();

    Single<WeatherDataModel> getFreshCurrentWeather();

}
