package com.example.julia.weatherguide.di;

import com.example.julia.weatherguide.repositories.SettingsRepositoryImpl;
import com.example.julia.weatherguide.services.current_weather_refresh.CurrentWeatherRefreshDataService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by julia on 20.07.17.
 */
@Component(modules = {AppModule.class, DataModule.class, PreferencesModule.class})
@Singleton
public interface DataComponent {
    CurrentWeatherComponent plusCurrentWeatherComponent(CurrentWeatherModule weatherModule);

    void inject(SettingsRepositoryImpl repoImpl);
    void inject(CurrentWeatherRefreshDataService refreshService);
}
