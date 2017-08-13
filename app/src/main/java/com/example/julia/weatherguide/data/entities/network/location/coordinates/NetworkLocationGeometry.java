package com.example.julia.weatherguide.data.entities.network.location.coordinates;

import com.google.gson.annotations.SerializedName;


public class NetworkLocationGeometry {

    @SerializedName("location")
    private NetworkLocation location;

    public NetworkLocationGeometry(NetworkLocation location) {
        this.location = location;
    }

    public NetworkLocation getLocation() {
        return location;
    }

}
