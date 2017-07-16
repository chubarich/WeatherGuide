package com.example.julia.weatherguide.repositories;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.julia.weatherguide.repositories.data.SharedPreferenceService;
import com.example.julia.weatherguide.repositories.network.OpenWeatherMapNetworkService;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by julia on 15.07.17.
 */

public class CurrentWeatherRepositoryImpl implements CurrentWeatherRepository {

    private static final String TAG = CurrentWeatherRepositoryImpl.class.getSimpleName();

    @Override
    public Observable<CurrentWeatherDataModel> getCurrentWeatherForLocation(@NonNull String location) {
            return SharedPreferenceService.getService().getCurrentWeather(location)
                    .switchIfEmpty(getFreshCurrentWeatherForLocation(location));
    }

    @Override
    public Observable<CurrentWeatherDataModel> getFreshCurrentWeatherForLocation(@NonNull String location) {
        Observable<CurrentWeatherDataModel> dataFromNetwork = OpenWeatherMapNetworkService.getService().getCurrentWeather(location);
        return dataFromNetwork.doOnNext(data -> {
                    SharedPreferenceService.getService().saveToSharedPreferences(data);
                });
    }

    @Override
    public String getCurrentLocation() {
        return SharedPreferenceService.getService().getCurrentLocationId();
    }
}
