package com.example.julia.weatherguide.di;

import android.content.Context;

import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractorImpl;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by julia on 20.07.17.
 */
@Module
public class CurrentWeatherModule {

    /*@Provides
    @WeatherScope
    public CurrentWeatherRepository provideCurrentWeatherRepository() {
        return new CurrentWeatherRepositoryImpl();
    }*/

    @Provides
    @WeatherScope
    public CurrentWeatherInteractor provideCurrentWeatherInteractor(Context context, CurrentWeatherRepository repo) {
        return new CurrentWeatherInteractorImpl(context, repo);
    }
}
