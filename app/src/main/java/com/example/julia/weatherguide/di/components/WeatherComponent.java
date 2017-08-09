package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.per_screen.SchedulerModule;
import com.example.julia.weatherguide.di.modules.per_screen.UseCasesModule;
import com.example.julia.weatherguide.di.modules.per_screen.WeatherModule;
import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.presentation.weather.WeatherFragment;

import dagger.Subcomponent;


@Subcomponent(modules = {SchedulerModule.class, UseCasesModule.class, WeatherModule.class})
@PerScreen
public interface WeatherComponent {

    void inject(WeatherFragment weatherFragment);

}
