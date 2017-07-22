package com.example.julia.weatherguide.repositories;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface CurrentWeatherRepository {

  Single<CurrentWeatherDataModel> getCurrentWeatherForLocation(@NonNull String location);

  Single<CurrentWeatherDataModel> getFreshCurrentWeatherForLocation(@NonNull String location);

  String getCurrentLocation();
}
