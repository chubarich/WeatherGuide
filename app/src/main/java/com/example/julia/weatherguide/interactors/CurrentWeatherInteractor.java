package com.example.julia.weatherguide.interactors;

import com.example.julia.weatherguide.data.entities.repository.WeatherDataModel;

import io.reactivex.Single;

public interface CurrentWeatherInteractor {

    Single<WeatherDataModel> getCurrentWeather();

    Single<WeatherDataModel> getFreshCurrentWeather();

}
