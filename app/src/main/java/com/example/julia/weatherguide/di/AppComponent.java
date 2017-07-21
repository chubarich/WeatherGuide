package com.example.julia.weatherguide.di;

import com.example.julia.weatherguide.repositories.SettingsRepositoryImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by julia on 20.07.17.
 */
@Component(modules = {AppModule.class, PreferencesModule.class})
@Singleton
public interface AppComponent {
    void inject (SettingsRepositoryImpl repo);
}
