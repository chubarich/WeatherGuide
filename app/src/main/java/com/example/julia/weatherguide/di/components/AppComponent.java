package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.MainViewModule;
import com.example.julia.weatherguide.di.modules.NetworkModule;
import com.example.julia.weatherguide.di.modules.SchedulerModule;
import com.example.julia.weatherguide.di.modules.SettingsModule;
import com.example.julia.weatherguide.di.modules.AppModule;
import com.example.julia.weatherguide.di.modules.CurrentWeatherModule;
import com.example.julia.weatherguide.di.modules.StorageModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    SettingsComponent plusSettingsComponent(SettingsModule settingsModule,
                                            SchedulerModule schedulerModule,
                                            NetworkModule networkModule,
                                            StorageModule storageModule);

    CurrentWeatherComponent plusCurrentWeatherComponent(CurrentWeatherModule currentWeatherModule,
                                                        SchedulerModule schedulerModule,
                                                        NetworkModule networkModule,
                                                        StorageModule storageModule);

    MainViewComponent plusMainViewComponent(MainViewModule mainViewModule,
                                            SchedulerModule schedulerModule,
                                            NetworkModule networkModule,
                                            StorageModule storageModule);

}
