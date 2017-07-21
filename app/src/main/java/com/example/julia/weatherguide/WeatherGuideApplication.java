package com.example.julia.weatherguide;

import android.app.Application;

import com.example.julia.weatherguide.di.AppModule;
import com.example.julia.weatherguide.di.CurrentWeatherComponent;
import com.example.julia.weatherguide.di.CurrentWeatherModule;
import com.example.julia.weatherguide.di.DaggerDataComponent;
import com.example.julia.weatherguide.di.DataComponent;
import com.example.julia.weatherguide.di.DataModule;
import com.example.julia.weatherguide.di.PreferencesModule;

/**
 * Created by julia on 15.07.17.
 */

public class WeatherGuideApplication extends Application {

    private static WeatherGuideApplication weatherGuideApp;
    private static DataComponent dataComponent;
    private static CurrentWeatherComponent currentWeatherComponent;

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
                .build();
    }

    public CurrentWeatherComponent plusCurrentWeatherComponent() {
        if (currentWeatherComponent == null) {
            currentWeatherComponent = dataComponent.plusCurrentWeatherComponent(new CurrentWeatherModule());
        }
        return currentWeatherComponent;
    }

    public void clearCurrentWeatherComponent() {
        currentWeatherComponent = null;
    }

}
