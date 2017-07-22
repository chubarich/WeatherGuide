package com.example.julia.weatherguide.di.components;


import com.example.julia.weatherguide.di.modules.SettingsModule;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.ui.settings.SettingsFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {SettingsModule.class})
@ScreenScope
public interface SettingsComponent {

  void inject(SettingsFragment settingsFragment);

}