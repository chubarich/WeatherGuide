package com.example.julia.weatherguide.di.modules;

import android.content.Context;

import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractorImpl;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepositoryImpl;
import com.example.julia.weatherguide.repositories.network.OpenWeatherMapNetworkService;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherPresenter;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CurrentWeatherModule {

  @Provides
  @ScreenScope
  SharedPreferenceService provideSharedPreferenceService(Context context) {
    return new SharedPreferenceService(context);
  }

  @Provides
  @ScreenScope
  OpenWeatherMapNetworkService provideOpenWeatherMapNetworkService(Context context) {
    return new OpenWeatherMapNetworkService(context);
  }

  @Provides
  @ScreenScope
  CurrentWeatherRepository provideRepository(SharedPreferenceService sharedPreferenceService,
                                                    OpenWeatherMapNetworkService openWeatherMapNetworkService) {
    return new CurrentWeatherRepositoryImpl(sharedPreferenceService, openWeatherMapNetworkService);
  }

  @Provides
  @ScreenScope
  public CurrentWeatherInteractor provideInteractor(CurrentWeatherRepository repository) {
    return new CurrentWeatherInteractorImpl(repository);
  }

  @Provides
  @ScreenScope
  public PresenterFactory<CurrentWeatherPresenter, CurrentWeatherView> providePresenterFactory(
      CurrentWeatherInteractor currentWeatherInteractor) {
    return new CurrentWeatherPresenter.Factory(currentWeatherInteractor);
  }

}
