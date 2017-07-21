package com.example.julia.weatherguide.di.modules;

import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.interactors.SettingsInteractorImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by julia on 21.07.17.
 */
@Module
public class SettingsModule {

    @Provides
    @Singleton
    public SettingsInteractor provideSettingsInteractor() {
        return new SettingsInteractorImpl();
    }
}
