package com.example.julia.weatherguide.data.entities.network.location.predictions;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class NetworkLocationPredictions {

    @SerializedName("predictions")
    private List<NetworkLocationPrediction> predictions;

    public List<NetworkLocationPrediction> getPredictions() {
        List<NetworkLocationPrediction> result = new ArrayList<>();
        for (NetworkLocationPrediction prediction : predictions) {
            result.add(prediction);
        }
        return result;
    }

}
