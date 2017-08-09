package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.per_screen.ChooseLocationModule;
import com.example.julia.weatherguide.di.modules.per_screen.MainModule;
import com.example.julia.weatherguide.di.modules.per_screen.SchedulerModule;
import com.example.julia.weatherguide.di.modules.per_screen.UseCasesModule;
import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.presentation.main.MainActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;


@Subcomponent(modules = {SchedulerModule.class, UseCasesModule.class, MainModule.class})
@PerScreen
public interface MainComponent {

    void inject(MainActivity mainActivity);

}
