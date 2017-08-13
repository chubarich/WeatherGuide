package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.data.services.refresh.RefreshDatabaseService;
import com.example.julia.weatherguide.di.modules.singleton.AppModule;
import com.example.julia.weatherguide.di.modules.singleton.DatabaseModule;
import com.example.julia.weatherguide.di.modules.singleton.NetworkModule;
import com.example.julia.weatherguide.di.modules.singleton.RepositoryModule;
import com.example.julia.weatherguide.di.modules.singleton.UtilsModule;

import javax.inject.Singleton;

import dagger.Component;


@Component(modules = {AppModule.class, DatabaseModule.class, NetworkModule.class, UtilsModule.class,
    RepositoryModule.class})
@Singleton
public interface AppComponent {

    MenuComponent plusMenuComponent();

    ChooseLocationComponent plusChooseLocationComponent();

    WeatherComponent plusWeatherComponent();

    MainComponent plusMainComponent();


    void inject(RefreshDatabaseService refreshWeatherService);

}
