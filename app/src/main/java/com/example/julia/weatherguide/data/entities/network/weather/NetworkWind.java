package com.example.julia.weatherguide.data.entities.network.weather;

import com.google.gson.annotations.SerializedName;

public class NetworkWind {

    @SerializedName("speed")
    private double speed;
    @SerializedName("deg")
    private double angle;

    public NetworkWind(double speed, double angle) {
        this.speed = speed;
        this.angle = angle;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }

}
