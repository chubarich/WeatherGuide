package com.example.julia.weatherguide.data.entities.presentation.weather;

import com.example.julia.weatherguide.utils.Preconditions;
import java.util.List;


public class Weather {

    private final CurrentWeather currentWeather;

    private final List<WeatherPrediction> weatherPredictions;


    public Weather(CurrentWeather currentWeather, List<WeatherPrediction> weatherPredictions) {
        Preconditions.nonNull(currentWeather, weatherPredictions);
        Preconditions.assertFalse(weatherPredictions.size() == 0);

        this.currentWeather = currentWeather;
        this.weatherPredictions = weatherPredictions;
    }

    public CurrentWeather getCurrent() {
        return currentWeather;
    }

    public int getPredictionsSize() {
        return weatherPredictions.size();
    }

    public WeatherPrediction getPredictionAt(int i) {
        return weatherPredictions.get(i);
    }
}
