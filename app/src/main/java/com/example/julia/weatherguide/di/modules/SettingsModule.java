package com.example.julia.weatherguide.di.modules;

import android.content.Context;

import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.interactors.SettingsInteractorImpl;
import com.example.julia.weatherguide.repositories.SettingsRepository;
import com.example.julia.weatherguide.repositories.SettingsRepositoryImpl;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.ui.settings.SettingsPresenter;
import com.example.julia.weatherguide.ui.settings.SettingsView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {

    @Provides
    @ScreenScope
    SettingsRepository provideRepository(Context context) {
        return new SettingsRepositoryImpl(context);
    }

    @Provides
    @ScreenScope
    SettingsInteractor provideInteractor(Context context, SettingsRepository settingsRepository) {
        return new SettingsInteractorImpl(context, settingsRepository);
    }

    @Provides
    @ScreenScope
    PresenterFactory<SettingsPresenter, SettingsView> providePresenterFactory(SettingsInteractor settingsInteractor) {
        return new SettingsPresenter.Factory(settingsInteractor);
    }

}
