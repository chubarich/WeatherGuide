package com.example.julia.weatherguide.data.entities.network.location.coordinates;

import com.google.gson.annotations.SerializedName;


public class NetworkLocationResult {

    @SerializedName("geometry")
    private NetworkLocationGeometry geometry;

    public NetworkLocationResult(NetworkLocationGeometry geometry) {
        this.geometry = geometry;
    }

    public NetworkLocationGeometry getGeometry() {
        return geometry;
    }

}
