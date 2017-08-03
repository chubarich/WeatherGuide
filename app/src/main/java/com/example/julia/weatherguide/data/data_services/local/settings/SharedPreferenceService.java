package com.example.julia.weatherguide.data.data_services.local.settings;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.example.julia.weatherguide.data.entities.repository.Location;
import com.example.julia.weatherguide.data.entities.repository.WeatherDataModel;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SharedPreferenceService implements SettingsService {

    private final SharedPreferences sharedPreferences;

    public SharedPreferenceService(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


}
