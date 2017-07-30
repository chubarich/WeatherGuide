package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.MainViewModule;
import com.example.julia.weatherguide.di.modules.NetworkModule;
import com.example.julia.weatherguide.di.modules.SchedulerModule;
import com.example.julia.weatherguide.di.modules.StorageModule;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.ui.main.MainActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {MainViewModule.class, SchedulerModule.class,
    StorageModule.class, NetworkModule.class})
@ScreenScope
public interface MainViewComponent {

    void inject(MainActivity mainActivity);

}
