package com.example.julia.weatherguide.di.modules;

import com.example.julia.weatherguide.di.qualifiers.UiScheduler;
import com.example.julia.weatherguide.di.qualifiers.WorkerScheduler;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.interactors.MainViewInteractor;
import com.example.julia.weatherguide.interactors.MainViewInteractorImpl;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepositoryImpl;
import com.example.julia.weatherguide.repositories.network.NetworkService;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.ui.main.MainPresenter;
import com.example.julia.weatherguide.ui.main.MainView;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module
public class MainViewModule {

    @Provides
    @ScreenScope
    CurrentWeatherRepository provideCurrentWeatherRepository(SharedPreferenceService sharedPreferenceService,
                                                             NetworkService networkService) {
        return new CurrentWeatherRepositoryImpl(sharedPreferenceService, networkService);
    }

    @Provides
    @ScreenScope
    MainViewInteractor provideMainViewInteractor(CurrentWeatherRepository currentWeatherRepository,
                                                 @WorkerScheduler Scheduler workerScheduler,
                                                 @UiScheduler Scheduler uiScheduler) {
        return new MainViewInteractorImpl(currentWeatherRepository, workerScheduler, uiScheduler);
    }

    @Provides
    @ScreenScope
    PresenterFactory<MainPresenter, MainView> providePresenterFactory(MainViewInteractor mainViewInteractor) {
        return new MainPresenter.Factory(mainViewInteractor);
    }

}
