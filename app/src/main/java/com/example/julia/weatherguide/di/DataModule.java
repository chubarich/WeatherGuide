package com.example.julia.weatherguide.di;

import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepositoryImpl;

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
}
