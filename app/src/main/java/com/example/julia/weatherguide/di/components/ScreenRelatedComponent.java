package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.ScreenRelatedModule;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherFragment;
import com.example.julia.weatherguide.ui.settings.SettingsFragment;

import dagger.Subcomponent;

/**
 * Created by julia on 21.07.17.
 */
@Subcomponent(modules = {ScreenRelatedModule.class})
@ScreenScope
public interface ScreenRelatedComponent {
    void inject(CurrentWeatherFragment weatherFragment);
    void inject(SettingsFragment settingsFragment);
}
