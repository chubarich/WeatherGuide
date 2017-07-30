package com.example.julia.weatherguide.di.modules;

import android.content.Context;

import com.example.julia.weatherguide.di.qualifiers.UiScheduler;
import com.example.julia.weatherguide.di.qualifiers.WorkerScheduler;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.interactors.SettingsInteractorImpl;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepositoryImpl;
import com.example.julia.weatherguide.repositories.network.NetworkService;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.ui.settings.SettingsPresenter;
import com.example.julia.weatherguide.ui.settings.SettingsView;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class SettingsModule {

    @Provides
    @ScreenScope
    Picasso providePicasso(Context context) {
        return Picasso.with(context);
    }

    @Provides
    @ScreenScope
    CurrentWeatherRepository provideRepository(SharedPreferenceService sharedPreferenceService,
                                               NetworkService openWeatherMapNetworkService,
                                               Picasso picasso) {
        return new CurrentWeatherRepositoryImpl(sharedPreferenceService, openWeatherMapNetworkService,
            picasso);
    }

    @Provides
    @ScreenScope
    SettingsInteractor provideInteractor(Context context,
                                         CurrentWeatherRepository currentWeatherRepository,
                                         @WorkerScheduler Scheduler workerScheduler,
                                         @UiScheduler Scheduler mainScheduler) {
        return new SettingsInteractorImpl(context, currentWeatherRepository,
            workerScheduler, mainScheduler);
    }

    @Provides
    @ScreenScope
    PresenterFactory<SettingsPresenter, SettingsView> providePresenterFactory(
        SettingsInteractor settingsInteractor) {
        return new SettingsPresenter.Factory(settingsInteractor);
    }

}
