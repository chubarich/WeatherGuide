package com.example.julia.weatherguide.repositories.network;

import com.example.julia.weatherguide.repositories.network.weather_data.WeatherInCity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by julia on 13.07.17.
 */

public interface OpenWeatherMapAPI {

    @GET("data/2.5/weather")
    Observable<WeatherInCity> getCurrentWeatherForLocation(@Query("id") String cityId);

}
