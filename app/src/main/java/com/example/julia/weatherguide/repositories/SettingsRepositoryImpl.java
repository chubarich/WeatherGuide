package com.example.julia.weatherguide.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.WeatherGuideApplication;

import io.reactivex.Observable;

/**
 * Created by julia on 16.07.17.
 */

public class SettingsRepositoryImpl implements SettingsRepository {

    private Context context;
    private final SharedPreferences sharedPreferences;

    public SettingsRepositoryImpl(@NonNull Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    @Override
    public Observable<Long> getRefreshIntervalChange() {
        return Observable.create(emitter -> {
            final SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    if (s.equals(context.getString(R.string.refresh_key))) {
                        int defaultInterval = context.getResources().getInteger(R.integer.default_refresh_interval);
                        emitter.onNext((long) sharedPreferences.getInt(s, defaultInterval));
                    }
                }
            };
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
            emitter.setCancellable(() -> sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener));
        });
    }

    @Override
    public void saveRefreshInterval(long interval) {
        sharedPreferences.edit()
                .putLong(context.getString(R.string.refresh_key), interval)
                .apply();
    }


}
