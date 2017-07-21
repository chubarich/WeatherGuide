package com.example.julia.weatherguide.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepositoryImpl;
import com.example.julia.weatherguide.repositories.SettingsRepository;
import com.example.julia.weatherguide.repositories.SettingsRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by julia on 20.07.17.
 */

@Module
public class DataModule {

    @Provides
    @Singleton
    public CurrentWeatherRepository provideCurrentWeatherRepository() {
        return new CurrentWeatherRepositoryImpl();
    }

    @Provides
    @Singleton
    public SettingsRepository provideSettingsRepository(@NonNull Context context) {
        return new SettingsRepositoryImpl(context);
    }
}
