package com.example.julia.weatherguide.repositories;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;
import com.example.julia.weatherguide.repositories.network.NetworkService;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;

import io.reactivex.Completable;
import io.reactivex.Single;

public class CurrentWeatherRepositoryImpl implements CurrentWeatherRepository {

    private final SharedPreferenceService sharedPreferenceService;
    private final NetworkService openWeatherMapNetworkService;

    public CurrentWeatherRepositoryImpl(SharedPreferenceService sharedPreferenceService,
                                        NetworkService networkService) {
        this.sharedPreferenceService = sharedPreferenceService;
        this.openWeatherMapNetworkService = networkService;
    }

    @Override
    public Single<WeatherDataModel> getCurrentWeather() {
        return getFreshCurrentWeather()
            .onErrorResumeNext(sharedPreferenceService.getCurrentWeather());
    }

    @Override
    public Single<WeatherDataModel> getFreshCurrentWeather() {
        if (!isLocationInitialized()) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED));
        } else {
            return openWeatherMapNetworkService.getCurrentWeather(sharedPreferenceService.getCurrentLocation())
                .flatMap(weatherDataModel ->
                    sharedPreferenceService.saveWeatherForCurrentLocation(weatherDataModel)
                        .onErrorComplete()
                        .toSingle(() -> {
                            weatherDataModel.setLocationName(sharedPreferenceService.getCurrentLocationName());
                            return weatherDataModel;
                        })
                );
        }
    }

    @Override
    public Completable saveCurrentLocation(final Location location, final String cityName) {
        return sharedPreferenceService.saveCurrentLocation(location, cityName);
    }

    @Override
    public boolean isLocationInitialized() {
        return sharedPreferenceService.isLocationInitialized();
    }
}
