package com.example.julia.weatherguide.interactors;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CurrentWeatherInteractor {

  Single<CurrentWeatherDataModel> getCurrentWeatherForLocation(@NonNull String location);

  Single<CurrentWeatherDataModel> getFreshCurrentWeatherForLocation(@NonNull String location);

  String getCurrentLocation();

  void destroy();

}
