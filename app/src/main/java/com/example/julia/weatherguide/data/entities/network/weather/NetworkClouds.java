package com.example.julia.weatherguide.data.entities.network.weather;

import com.google.gson.annotations.SerializedName;


public class NetworkClouds {

    @SerializedName("all")
    private double cloudiness;

    public double getCloudiness() {
        return cloudiness;
    }

}
