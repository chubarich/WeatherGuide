package com.example.julia.weatherguide;

import android.app.Application;

/**
 * Created by julia on 15.07.17.
 */

public class WeatherGuideApplication extends Application {

    private static WeatherGuideApplication weatherGuideApp;

    public static WeatherGuideApplication getInstance() {
        return weatherGuideApp;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        weatherGuideApp = this;
    }

}
