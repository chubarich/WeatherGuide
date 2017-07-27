package com.example.julia.weatherguide.repositories.network;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;

import io.reactivex.Single;

public interface NetworkService {

    Single<WeatherDataModel> getCurrentWeather(Location location);

}
