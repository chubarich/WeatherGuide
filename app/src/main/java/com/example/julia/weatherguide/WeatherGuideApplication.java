package com.example.julia.weatherguide;

import android.app.Application;

import com.example.julia.weatherguide.di.components.DaggerDataComponent;
import com.example.julia.weatherguide.di.modules.AppModule;
import com.example.julia.weatherguide.di.modules.CurrentWeatherModule;
import com.example.julia.weatherguide.di.components.DataComponent;
import com.example.julia.weatherguide.di.modules.DataModule;
import com.example.julia.weatherguide.di.modules.PreferencesModule;
import com.example.julia.weatherguide.di.components.ScreenRelatedComponent;
import com.example.julia.weatherguide.di.modules.ScreenRelatedModule;
import com.example.julia.weatherguide.di.modules.SettingsModule;

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
