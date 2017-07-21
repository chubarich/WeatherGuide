package com.example.julia.weatherguide.interactors;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;

import io.reactivex.Observable;

/**
 * Created by julia on 15.07.17.
 */

public interface CurrentWeatherInteractor {
    Observable<CurrentWeatherDataModel> getCurrentWeatherForLocation(@NonNull String location);
    Observable<CurrentWeatherDataModel> getFreshCurrentWeatherForLocation(@NonNull String location);
    void scheduleForUpdateCurrentWeather(int interval);
    String getCurrentLocation();
}
