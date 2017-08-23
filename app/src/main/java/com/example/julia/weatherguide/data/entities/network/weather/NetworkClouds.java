package com.example.julia.weatherguide.data.entities.network.weather;

import com.google.gson.annotations.SerializedName;


public class NetworkClouds {

    @SerializedName("all")
    private double cloudiness;

    public NetworkClouds(double cloudiness) {
        this.cloudiness = cloudiness;
    }

    public double getCloudiness() {
        return cloudiness;
    }

}
