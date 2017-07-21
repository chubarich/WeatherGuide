package com.example.julia.weatherguide.di.modules;

import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherPresenter;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherPresenterImpl;
import com.example.julia.weatherguide.ui.settings.SettingsPresenter;
import com.example.julia.weatherguide.ui.settings.SettingsPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by julia on 21.07.17.
 */
@Module
public class ScreenRelatedModule {

    @Provides
    @ScreenScope
    public CurrentWeatherPresenter provideCurrentWeatherPresenter() {
        return new CurrentWeatherPresenterImpl();
    }

    @Provides
    @ScreenScope
    public SettingsPresenter provideSettingsPreseter() {
        return new SettingsPresenterImpl();
    }
}
