package com.example.julia.weatherguide.data.entities.network.weather;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class NetworkWeatherPredictions {

    @SerializedName("list")
    private List<NetworkWeatherPrediction> predictions;

    @SerializedName("cod")
    private int code;

    public NetworkWeatherPredictions(List<NetworkWeatherPrediction> predictions) {
        predictions = new ArrayList<>();
        predictions.addAll(predictions);
    }

    public List<NetworkWeatherPrediction> getPredictions() {
        List<NetworkWeatherPrediction> result = new ArrayList<>();
        for (NetworkWeatherPrediction prediction : predictions) {
            result.add(prediction);
        }
        return result;
    }

    public int getCode() {
        return code;
    }
}
