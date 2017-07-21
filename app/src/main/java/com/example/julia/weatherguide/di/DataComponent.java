package com.example.julia.weatherguide.di;

import com.example.julia.weatherguide.interactors.CurrentWeatherInteractorImpl;
import com.example.julia.weatherguide.interactors.SettingsInteractorImpl;
import com.example.julia.weatherguide.repositories.SettingsRepositoryImpl;
import com.example.julia.weatherguide.services.current_weather_refresh.CurrentWeatherRefreshDataService;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherPresenterImpl;
import com.example.julia.weatherguide.ui.settings.SettingsPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by julia on 20.07.17.
 */
@Component(modules = {AppModule.class, DataModule.class, PreferencesModule.class, SettingsModule.class, CurrentWeatherModule.class})
@Singleton
public interface DataComponent {
    ScreenRelatedComponent plusScreenRelatedComponent(ScreenRelatedModule screenModule);

    void inject(SettingsRepositoryImpl repoImpl);
    void inject(CurrentWeatherRefreshDataService refreshService);
    void inject(SettingsPresenterImpl settingsPresenter);
    void inject(CurrentWeatherPresenterImpl weatherPresenter);
    void inject(SettingsInteractorImpl settingsInteractor);
    void inject(CurrentWeatherInteractorImpl weatherInteractor);
}
