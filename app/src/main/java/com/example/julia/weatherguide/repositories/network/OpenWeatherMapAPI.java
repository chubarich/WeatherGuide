package com.example.julia.weatherguide.repositories.network;

import com.example.julia.weatherguide.repositories.network.weather_data.WeatherInCity;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface OpenWeatherMapAPI {

    @GET("data/2.5/weather")
    Single<WeatherInCity> getCurrentWeatherForLocation(@Query("id") String cityId);

}
