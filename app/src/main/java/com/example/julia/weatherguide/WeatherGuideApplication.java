package com.example.julia.weatherguide;

import android.app.Application;

import com.example.julia.weatherguide.di.components.CurrentWeatherComponent;
import com.example.julia.weatherguide.di.components.DaggerDataComponent;
import com.example.julia.weatherguide.di.components.SettingsComponent;
import com.example.julia.weatherguide.di.modules.AppModule;
import com.example.julia.weatherguide.di.components.DataComponent;
import com.example.julia.weatherguide.di.modules.CurrentWeatherModule;
import com.example.julia.weatherguide.di.modules.SettingsModule;

public class WeatherGuideApplication extends Application {

  private DataComponent dataComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    dataComponent = DaggerDataComponent.builder()
        .appModule(new AppModule(getApplicationContext()))
        .build();
  }

  public SettingsComponent getSettingsComponent() {
    return dataComponent.plusSettingsComponent(new SettingsModule());
  }

  public CurrentWeatherComponent getCurrentWeatherComponent() {
    return dataComponent.plusCurrentWeatherComponent(new CurrentWeatherModule());
  }

  public DataComponent getDataComponent() {
    return dataComponent;
  }

}
