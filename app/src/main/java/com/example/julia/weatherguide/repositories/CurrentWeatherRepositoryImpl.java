package com.example.julia.weatherguide.repositories;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;
import com.example.julia.weatherguide.repositories.network.OpenWeatherMapNetworkService;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class CurrentWeatherRepositoryImpl implements CurrentWeatherRepository {

  private final SharedPreferenceService sharedPreferenceService;
  private final OpenWeatherMapNetworkService openWeatherMapNetworkService;

  public CurrentWeatherRepositoryImpl(SharedPreferenceService sharedPreferenceService,
                                      OpenWeatherMapNetworkService openWeatherMapNetworkService) {
    this.sharedPreferenceService = sharedPreferenceService;
    this.openWeatherMapNetworkService = openWeatherMapNetworkService;
  }

  @Override
  public Single<CurrentWeatherDataModel> getCurrentWeatherForLocation(@NonNull String location) {
    return sharedPreferenceService.getCurrentWeather(location)
        .switchIfEmpty(getFreshCurrentWeatherForLocation(location).toMaybe())
        .toSingle();
  }

  @Override
  public Single<CurrentWeatherDataModel> getFreshCurrentWeatherForLocation(@NonNull String location) {
    return openWeatherMapNetworkService.getCurrentWeather(location)
        .doOnSuccess(sharedPreferenceService::saveToSharedPreferences);
  }

  @Override
  public String getCurrentLocation() {
    return sharedPreferenceService.getCurrentLocationId();
  }
}
