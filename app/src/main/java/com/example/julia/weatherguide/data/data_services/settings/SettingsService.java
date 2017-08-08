package com.example.julia.weatherguide.data.data_services.settings;

import io.reactivex.Observable;

public interface SettingsService {

    boolean isTemperatureTypeInFahrenheit();

    boolean isWeatherSpeedInKph();

    boolean isPressureInHpa();

    long currentLocationId();

    void setCurrentLocationId(long id);

    Observable<Long> subscribeOnCurrentLocationIdChanges();

}
