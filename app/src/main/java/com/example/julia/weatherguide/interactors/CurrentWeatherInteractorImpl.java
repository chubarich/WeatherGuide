package com.example.julia.weatherguide.interactors;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import io.reactivex.Single;

public class CurrentWeatherInteractorImpl implements CurrentWeatherInteractor {

  private CurrentWeatherRepository repository;

  public CurrentWeatherInteractorImpl(CurrentWeatherRepository currentWeatherRepository) {
    this.repository = currentWeatherRepository;
  }

  @Override
  public void destroy() {
    this.repository = null;
  }

  @Override
  public Single<CurrentWeatherDataModel> getCurrentWeatherForLocation(@NonNull String location) {
    return repository.getCurrentWeatherForLocation(location);
  }

  @Override
  public Single<CurrentWeatherDataModel> getFreshCurrentWeatherForLocation(@NonNull String location) {
    return repository.getFreshCurrentWeatherForLocation(location);
  }

  @Override
  public String getCurrentLocation() {
    return repository.getCurrentLocation();
  }

}
