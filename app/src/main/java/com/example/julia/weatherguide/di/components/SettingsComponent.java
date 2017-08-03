package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.NetworkModule;
import com.example.julia.weatherguide.di.modules.SchedulerModule;
import com.example.julia.weatherguide.di.modules.SettingsModule;
import com.example.julia.weatherguide.di.modules.StorageModule;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.presentation.settings.SettingsFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {SettingsModule.class, SchedulerModule.class,
    StorageModule.class, NetworkModule.class})
@ScreenScope
public interface SettingsComponent {

    void inject(SettingsFragment settingsFragment);

}