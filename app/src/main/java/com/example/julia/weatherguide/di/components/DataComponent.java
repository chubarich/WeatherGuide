package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.SettingsModule;
import com.example.julia.weatherguide.di.modules.AppModule;
import com.example.julia.weatherguide.di.modules.CurrentWeatherModule;
import com.example.julia.weatherguide.services.refresh.CurrentWeatherRefreshDataService;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface DataComponent {

    SettingsComponent plusSettingsComponent(SettingsModule settingsModule);

    CurrentWeatherComponent plusCurrentWeatherComponent(CurrentWeatherModule currentWeatherModule);

}
