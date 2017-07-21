package com.example.julia.weatherguide.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.WeatherGuideApplication;

import javax.inject.Inject;

/**
 * Created by julia on 16.07.17.
 */

public class SettingsRepositoryImpl implements SettingsRepository {

    @Inject
    Context context;

    @Inject
    SharedPreferences sharedPreferences;

    public SettingsRepositoryImpl(@NonNull Context context) {
        WeatherGuideApplication.getDataComponent().inject(this);
    }

    @Override
    public void saveRefreshInterval(long interval) {
        sharedPreferences.edit()
                .putLong(context.getString(R.string.refresh_key), interval)
                .apply();
    }


}
