package com.example.julia.weatherguide.data.data_services.settings;

import android.content.SharedPreferences;
import com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract;
import com.example.julia.weatherguide.data.entities.repository.weather.WeatherNotification;
import com.example.julia.weatherguide.utils.Optional;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract.KEY_CURRENT_LOCATION_ID;
import static com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract.KEY_PRESSURE_IN_HPA;
import static com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract.KEY_TEMPERATURE_IN_FAHRENHEIT;
import static com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract.KEY_WEATHER_SPEED_IN_KPH;


public class SharedPreferenceService implements SettingsService {

    private final Subject<Optional<Long>> currentLocationChangeSubject;
    private final SharedPreferences sharedPreferences;


    public SharedPreferenceService(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        currentLocationChangeSubject = BehaviorSubject.<Optional<Long>>create()
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
    public Long currentLocationId() {
        long id = sharedPreferences.getLong(KEY_CURRENT_LOCATION_ID, -1);
        return id == -1 ? null : id;
    }

    @Override
    public void setCurrentLocationId(long id) {
        boolean success = sharedPreferences.edit()
            .putLong(KEY_CURRENT_LOCATION_ID, id)
            .commit();

        if (success) currentLocationChangeSubject.onNext(Optional.of(id));
    }

    @Override
    public Observable<Optional<Long>> subscribeOnCurrentLocationIdChanges() {
        currentLocationChangeSubject.onNext(Optional.of(currentLocationId()));
        return currentLocationChangeSubject;
    }
}
