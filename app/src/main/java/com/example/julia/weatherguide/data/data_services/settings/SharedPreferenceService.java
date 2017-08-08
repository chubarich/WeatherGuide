package com.example.julia.weatherguide.data.data_services.settings;

import android.content.SharedPreferences;
import com.example.julia.weatherguide.data.contracts.local.settings.SettingsContract;


public class SharedPreferenceService implements SettingsService {

    private final SharedPreferences sharedPreferences;

    public SharedPreferenceService(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public boolean isTemperatureTypeInFahrenheit() {
        return sharedPreferences.getBoolean(SettingsContract.KEY_TEMPERATURE_IN_FAHRENHEIT, false);
    }

    @Override
    public boolean isWeatherSpeedInKph() {
        return sharedPreferences.getBoolean(SettingsContract.KEY_WEATHER_SPEED_IN_KPH, false);
    }

    @Override
    public boolean isPressureInHpa() {
        return sharedPreferences.getBoolean(SettingsContract.KEY_PRESSURE_IN_HPA, false);
    }
}
