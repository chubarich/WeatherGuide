package com.example.julia.weatherguide.data.repositories.weather;

import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.data.entities.repository.weather.WeatherNotification;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy;

public interface WeatherRepository {

    Single<Weather> getWeather(LocationWithId location, GetWeatherStrategy getWeatherStrategy);

    Observable<WeatherNotification> subscribeOnCurrentWeatherChanges();

    Completable deleteWeather(LocationWithId location);

    Integer getTemperature(LocationWithId location);

}
