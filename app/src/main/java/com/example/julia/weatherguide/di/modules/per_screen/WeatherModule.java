package com.example.julia.weatherguide.di.modules.per_screen;

import com.example.julia.weatherguide.data.managers.NetworkManager;
import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.domain.use_cases.GetWeatherUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.weather.WeatherPresenter;
import com.example.julia.weatherguide.presentation.weather.WeatherView;

import dagger.Module;
import dagger.Provides;


@Module
public class WeatherModule {

    @Provides
    @PerScreen
    PresenterFactory<WeatherPresenter, WeatherView> providePresenterFactory(GetWeatherUseCase getWeatherUseCase,
                                                                            NetworkManager networkManager) {
        return new WeatherPresenter.Factory(getWeatherUseCase, networkManager);
    }

}
