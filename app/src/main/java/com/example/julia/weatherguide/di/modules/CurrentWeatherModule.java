package com.example.julia.weatherguide.di.modules;

import android.content.Context;

import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractorImpl;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by julia on 20.07.17.
 */
@Module
public class CurrentWeatherModule {
    @Provides
    @Singleton
    public CurrentWeatherInteractor provideCurrentWeatherInteractor(Context context, CurrentWeatherRepository repo) {
        return new CurrentWeatherInteractorImpl();
    }
}
