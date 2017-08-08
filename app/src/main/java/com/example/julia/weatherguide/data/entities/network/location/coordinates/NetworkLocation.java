package com.example.julia.weatherguide.data.entities.network.location.coordinates;


import com.google.gson.annotations.SerializedName;

public class NetworkLocation {

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
