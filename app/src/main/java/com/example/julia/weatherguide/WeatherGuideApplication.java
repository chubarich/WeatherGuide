package com.example.julia.weatherguide;

import android.app.Application;

import com.example.julia.weatherguide.di.AppModule;
import com.example.julia.weatherguide.di.CurrentWeatherModule;
import com.example.julia.weatherguide.di.DaggerDataComponent;
import com.example.julia.weatherguide.di.DataComponent;
import com.example.julia.weatherguide.di.DataModule;
import com.example.julia.weatherguide.di.PreferencesModule;
import com.example.julia.weatherguide.di.ScreenRelatedComponent;
import com.example.julia.weatherguide.di.ScreenRelatedModule;
import com.example.julia.weatherguide.di.SettingsModule;

/**
 * Created by julia on 15.07.17.
 */

public class WeatherGuideApplication extends Application {

    private static WeatherGuideApplication weatherGuideApp;
    private static DataComponent dataComponent;
    private static ScreenRelatedComponent screenRelatedComponent;

    public static WeatherGuideApplication getInstance() {
        return weatherGuideApp;
    }

    public static DataComponent getDataComponent() {
        return dataComponent;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        weatherGuideApp = this;
        dataComponent = DaggerDataComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .dataModule(new DataModule())
                .preferencesModule(new PreferencesModule())
                .settingsModule(new SettingsModule())
                .currentWeatherModule(new CurrentWeatherModule())
                .build();
    }


    public ScreenRelatedComponent plusScreenRelatedComponent() {
        if (screenRelatedComponent == null) {
            screenRelatedComponent = dataComponent.plusScreenRelatedComponent(new ScreenRelatedModule());
        }
        return screenRelatedComponent;
    }

    public void clearScreenRelatedComponent() {
        screenRelatedComponent = null;
    }

}
