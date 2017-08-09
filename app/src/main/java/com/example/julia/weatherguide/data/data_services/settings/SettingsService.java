package com.example.julia.weatherguide.data.data_services.settings;

import com.example.julia.weatherguide.utils.Optional;

import io.reactivex.Observable;

public interface SettingsService {

    boolean isTemperatureTypeInFahrenheit();

    boolean isWeatherSpeedInKph();

    boolean isPressureInHpa();

    Long currentLocationId();

    void setCurrentLocationId(long id);

    Observable<Optional<Long>> subscribeOnCurrentLocationIdChanges();

}
