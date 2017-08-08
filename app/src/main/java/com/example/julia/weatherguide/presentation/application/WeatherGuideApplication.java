package com.example.julia.weatherguide.presentation.application;

import android.app.Application;

import com.example.julia.weatherguide.di.components.ChooseLocationComponent;
import com.example.julia.weatherguide.di.components.DaggerAppComponent;
import com.example.julia.weatherguide.di.components.MenuComponent;
import com.example.julia.weatherguide.di.modules.per_screen.MenuModule;
import com.example.julia.weatherguide.di.modules.singleton.AppModule;
import com.example.julia.weatherguide.di.components.AppComponent;
import com.example.julia.weatherguide.di.modules.per_screen.SchedulerModule;
import com.facebook.stetho.Stetho;


public class WeatherGuideApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(getApplicationContext()))
            .build();
    }

    public MenuComponent getMenuComponent() {
        return appComponent.plusMenuComponent();
    }

    public ChooseLocationComponent getChooseLocationComponent() {
        return appComponent.plusChooseLocationComponent();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


}
