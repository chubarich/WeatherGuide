package com.example.julia.weatherguide.data.entities.network.weather;


import com.google.gson.annotations.SerializedName;

public class NetworkTemperatures {

    @SerializedName("min")
    private double minTemperature;

    @SerializedName("max")
    private double maxTemperature;

    @SerializedName("morn")
    private double morningTemperature;

    @SerializedName("day")
    private double dayTemperature;

    @SerializedName("night")
    private double nightTemperature;

    @SerializedName("eve")
    private double eveningTemperature;

    public NetworkTemperatures(double minTemperature, double maxTemperature, double morningTemperature,
                               double dayTemperature, double nightTemperature, double eveningTemperature) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.morningTemperature = morningTemperature;
        this.dayTemperature = dayTemperature;
        this.nightTemperature = nightTemperature;
        this.eveningTemperature = eveningTemperature;
    }


    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getMorningTemperature() {
        return morningTemperature;
    }

    public double getDayTemperature() {
        return dayTemperature;
    }

    public double getNightTemperature() {
        return nightTemperature;
    }

    public double getEveningTemperature() {
        return eveningTemperature;
    }
}