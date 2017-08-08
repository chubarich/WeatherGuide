package com.example.julia.weatherguide.data.contracts.network.weather;

import com.example.julia.weatherguide.data.entities.network.weather.NetworkCurrentWeather;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkWeatherPredictions;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import static com.example.julia.weatherguide.data.constants.OpenWeatherMapEndpoints.*;


public interface OpenWeatherMapContract {

    @GET(DATA + V2_5 + WEATHER)
    Single<NetworkCurrentWeather> getCurrentWeather(
        @Query(KEY_LATITUDE) float latitude,
        @Query(KEY_LONGITUDE) float longitude
    );

    @GET(DATA + V2_5 + FORECAST)
    Single<NetworkWeatherPredictions> getWeatherPrediction(
        @Query(KEY_LATITUDE) float latitude,
        @Query(KEY_LONGITUDE) float longitude,
        @Query(KEY_COUNT) int count
    );
}
