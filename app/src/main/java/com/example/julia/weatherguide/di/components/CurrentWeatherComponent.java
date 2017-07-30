package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.CurrentWeatherModule;
import com.example.julia.weatherguide.di.modules.NetworkModule;
import com.example.julia.weatherguide.di.modules.SchedulerModule;
import com.example.julia.weatherguide.di.modules.StorageModule;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.services.refresh.CurrentWeatherRefreshDataService;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {CurrentWeatherModule.class, SchedulerModule.class,
    StorageModule.class, NetworkModule.class})
@ScreenScope
public interface CurrentWeatherComponent {

    void inject(CurrentWeatherFragment weatherFragment);

    void inject(CurrentWeatherRefreshDataService refreshService);

}
