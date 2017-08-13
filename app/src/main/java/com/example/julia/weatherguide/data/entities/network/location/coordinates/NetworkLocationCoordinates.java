package com.example.julia.weatherguide.data.entities.network.location.coordinates;

import com.google.gson.annotations.SerializedName;


public class NetworkLocationCoordinates {

    @SerializedName("result")
    private NetworkLocationResult result;

    public NetworkLocationCoordinates(NetworkLocationResult result) {
        this.result = result;
    }

    public boolean isValid() {
        return result != null;
    }

    public double getLatitude() {
        return result.getGeometry().getLocation().getLatitude();
    }

    public double getLongitude() {
        return result.getGeometry().getLocation().getLongitude();
    }

}
