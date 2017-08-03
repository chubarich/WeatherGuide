package com.example.julia.weatherguide.data.contracts.network;

import com.example.julia.weatherguide.data.entities.remote.WeatherInCity;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapContract {

    @GET("data/2.5/weather")
    Single<WeatherInCity> getCurrentWeatherForLocation(
        @Query("lat") double latitude,
        @Query("lon") double longitude
    );

}
