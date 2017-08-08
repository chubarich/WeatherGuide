package com.example.julia.weatherguide.data.data_services.settings;

import android.content.SharedPreferences;
import com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract;
import com.example.julia.weatherguide.data.entities.repository.weather.WeatherNotification;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract.KEY_CURRENT_LOCATION_ID;
import static com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract.KEY_PRESSURE_IN_HPA;
import static com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract.KEY_TEMPERATURE_IN_FAHRENHEIT;
import static com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract.KEY_WEATHER_SPEED_IN_KPH;


public class SharedPreferenceService implements SettingsService {

    private final Subject<Long> currentLocationChangeSubject;
    private final SharedPreferences sharedPreferences;


    public SharedPreferenceService(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        currentLocationChangeSubject = BehaviorSubject.<Long>create()
            .toSerialized();
    }

    @Override
    public boolean isTemperatureTypeInFahrenheit() {
        return sharedPreferences.getBoolean(KEY_TEMPERATURE_IN_FAHRENHEIT, false);
    }

    @Override
    public boolean isWeatherSpeedInKph() {
        return sharedPreferences.getBoolean(KEY_WEATHER_SPEED_IN_KPH, false);
    }

    @Override
    public boolean isPressureInHpa() {
        return sharedPreferences.getBoolean(KEY_PRESSURE_IN_HPA, false);
    }

    @Override
    public long currentLocationId() {
        return sharedPreferences.getLong(KEY_CURRENT_LOCATION_ID, -1);
    }

    @Override
    public void setCurrentLocationId(long id) {
        boolean success = sharedPreferences.edit()
            .putLong(KEY_CURRENT_LOCATION_ID, id)
            .commit();

        if (success) currentLocationChangeSubject.onNext(id);
    }

    @Override
    public Observable<Long> subscribeOnCurrentLocationIdChanges() {
        currentLocationChangeSubject.onNext(currentLocationId());
        return currentLocationChangeSubject;
    }
}
