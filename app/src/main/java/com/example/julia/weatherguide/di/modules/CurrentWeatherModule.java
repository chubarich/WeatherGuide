package com.example.julia.weatherguide.di.modules;

import com.example.julia.weatherguide.di.qualifiers.UiScheduler;
import com.example.julia.weatherguide.di.qualifiers.WorkerScheduler;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractorImpl;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepositoryImpl;
import com.example.julia.weatherguide.repositories.network.NetworkService;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherPresenter;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherView;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class CurrentWeatherModule {

    @Provides
    @ScreenScope
    CurrentWeatherRepository provideRepository(SharedPreferenceService sharedPreferenceService,
                                               NetworkService openWeatherMapNetworkService) {
        return new CurrentWeatherRepositoryImpl(sharedPreferenceService, openWeatherMapNetworkService);
    }

    @Provides
    @ScreenScope
    CurrentWeatherInteractor provideInteractor(CurrentWeatherRepository repository,
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
