package com.example.julia.weatherguide.data.entities.presentation.weather;

import com.example.julia.weatherguide.utils.Preconditions;
import java.util.List;


public class Weather {

    private final String locationName;

    private final CurrentWeather currentWeather;

    private final List<WeatherPrediction> weatherPrediction;


    public Weather(String locationName, CurrentWeather currentWeather,
                   List<WeatherPrediction> weatherPrediction) {
        Preconditions.nonNull(locationName, weatherPrediction);
        Preconditions.assertFalse(weatherPrediction.size() == 0);

        this.locationName = locationName;
        this.currentWeather = currentWeather;
        this.weatherPrediction = weatherPrediction;
    }
}
