package com.example.julia.weatherguide.di.modules;

import android.content.Context;

import com.example.julia.weatherguide.data.data_services.network.weather.NetworkWeatherService;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository;
import com.example.julia.weatherguide.di.qualifiers.UiScheduler;
import com.example.julia.weatherguide.di.qualifiers.WorkerScheduler;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractorImpl;
import com.example.julia.weatherguide.data.data_services.local.settings.SettingsService;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.current_weather.CurrentWeatherPresenter;
import com.example.julia.weatherguide.presentation.current_weather.CurrentWeatherView;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class CurrentWeatherModule {

    @Provides
    @ScreenScope
    Picasso providePicasso(Context context) {
        return Picasso.with(context);
    }

    @Provides
    @ScreenScope
    WeatherRepository provideRepository(SettingsService settingsService,
                                        NetworkWeatherService openWeatherMapNetworkService,
                                        Picasso picasso) {
        return new OpenWeatherMapRepository(settingsService, openWeatherMapNetworkService,
            picasso);
    }

    @Provides
    @ScreenScope
    CurrentWeatherInteractor provideInteractor(WeatherRepository repository,
                                               @WorkerScheduler Scheduler workerScheduler,
                                               @UiScheduler Scheduler uiScheduler) {
        return new CurrentWeatherInteractorImpl(repository, workerScheduler, uiScheduler);
    }

    @Provides
    @ScreenScope
    PresenterFactory<CurrentWeatherPresenter, CurrentWeatherView> providePresenterFactory(
        CurrentWeatherInteractor currentWeatherInteractor) {
        return new CurrentWeatherPresenter.Factory(currentWeatherInteractor);
    }

}
