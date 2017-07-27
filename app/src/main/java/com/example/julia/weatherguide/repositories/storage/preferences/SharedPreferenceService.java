package com.example.julia.weatherguide.repositories.storage.preferences;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public interface SharedPreferenceService {

    Single<WeatherDataModel> getCurrentWeather();

    Location getCurrentLocation();

    String getCurrentLocationName();

    Completable saveCurrentLocation(Location location, String cityName);

    Completable saveWeatherForCurrentLocation(@NonNull WeatherDataModel data);

    boolean isLocationInitialized();

}
