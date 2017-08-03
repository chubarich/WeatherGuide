package com.example.julia.weatherguide.di.modules;

import android.content.Context;

import com.example.julia.weatherguide.data.data_services.network.weather.NetworkWeatherService;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.di.qualifiers.UiScheduler;
import com.example.julia.weatherguide.di.qualifiers.WorkerScheduler;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.interactors.SettingsInteractorImpl;
import com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository;
import com.example.julia.weatherguide.data.data_services.local.settings.SettingsService;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.settings.SettingsPresenter;
import com.example.julia.weatherguide.presentation.settings.SettingsView;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class SettingsModule {

    @Provides
    @ScreenScope
    WeatherRepository provideRepository(SettingsService settingsService,
                                        NetworkWeatherService openWeatherMapNetworkService) {
        return new OpenWeatherMapRepository(settingsService, openWeatherMapNetworkService);
    }

    @Provides
    @ScreenScope
    SettingsInteractor provideInteractor(Context context,
                                         WeatherRepository weatherRepository,
                                         @WorkerScheduler Scheduler workerScheduler,
                                         @UiScheduler Scheduler mainScheduler) {
        return new SettingsInteractorImpl(context, weatherRepository,
            workerScheduler, mainScheduler);
    }

    @Provides
    @ScreenScope
    PresenterFactory<SettingsPresenter, SettingsView> providePresenterFactory(
        SettingsInteractor settingsInteractor) {
        return new SettingsPresenter.Factory(settingsInteractor);
    }

}
