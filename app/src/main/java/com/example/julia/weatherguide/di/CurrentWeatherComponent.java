package com.example.julia.weatherguide.di;

import com.example.julia.weatherguide.interactors.CurrentWeatherInteractorImpl;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherPresenter;

import dagger.Subcomponent;

/**
 * Created by julia on 20.07.17.
 */

@Subcomponent(modules = {CurrentWeatherModule.class})
@WeatherScope
public interface CurrentWeatherComponent {

    void inject(CurrentWeatherPresenter presenter);
    void inject(CurrentWeatherInteractorImpl interactorImpl);
}
