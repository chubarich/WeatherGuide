package com.example.julia.weatherguide.interactors;

public interface SettingsInteractor {

  void saveRefreshPeriod(long period);

  void scheduleForUpdateCurrentWeather(int interval);

  void destroy();

}
