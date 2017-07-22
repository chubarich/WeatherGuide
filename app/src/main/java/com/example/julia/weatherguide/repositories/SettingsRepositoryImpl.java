package com.example.julia.weatherguide.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.preference.PreferenceManager;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;

import javax.inject.Inject;

public class SettingsRepositoryImpl implements SettingsRepository {

  private final Context context;

  public SettingsRepositoryImpl(Context context) {
    this.context = context;
  }

  @Override
  public void saveRefreshInterval(long interval) {
    PreferenceManager.getDefaultSharedPreferences(context).edit()
        .putLong(context.getString(R.string.refresh_key), interval)
        .apply();
  }

}
