package com.example.julia.weatherguide.presentation.application;

import android.app.Application;

import com.example.julia.weatherguide.di.components.CurrentWeatherComponent;
import com.example.julia.weatherguide.di.components.DaggerAppComponent;
import com.example.julia.weatherguide.di.components.MainViewComponent;
import com.example.julia.weatherguide.di.components.SettingsComponent;
import com.example.julia.weatherguide.di.modules.AppModule;
import com.example.julia.weatherguide.di.components.AppComponent;
import com.example.julia.weatherguide.di.modules.CurrentWeatherModule;
import com.example.julia.weatherguide.di.modules.MainViewModule;
import com.example.julia.weatherguide.di.modules.NetworkModule;
import com.example.julia.weatherguide.di.modules.SchedulerModule;
import com.example.julia.weatherguide.di.modules.SettingsModule;
import com.example.julia.weatherguide.di.modules.StorageModule;

public class WeatherGuideApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(getApplicationContext()))
            .build();
    }

    public SettingsComponent getSettingsComponent() {
        return appComponent.plusSettingsComponent(new SettingsModule(), new SchedulerModule(),
            new NetworkModule(), new StorageModule());
    }

    public CurrentWeatherComponent getCurrentWeatherComponent() {
        return appComponent.plusCurrentWeatherComponent(new CurrentWeatherModule(), new SchedulerModule(),
            new NetworkModule(), new StorageModule());
    }

    public MainViewComponent getMainViewComponent() {
        return appComponent.plusMainViewComponent(new MainViewModule(), new SchedulerModule(),
            new NetworkModule(), new StorageModule());
    }

}
