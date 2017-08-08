package com.example.julia.weatherguide.data.entities.network.weather;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class NetworkWeatherPrediction {

    @SerializedName("dt")
    private long timestamp;

    @SerializedName("temp")
    private NetworkTemperatures temperatures;

    @SerializedName("pressure")
    private double pressure;

    @SerializedName("humidity")
    private double humidity;

    @SerializedName("condition")
    private List<NetworkCondition> condition;

    @SerializedName("speed")
    private double windSpeed;

    @SerializedName("deg")
    private double windAngle;

    @SerializedName("clouds")
    private double cloudiness;


    public long getTimestamp() {
        return timestamp;
    }

    public double getMinTemperature() {
        return temperatures.getMinTemperature();
    }

    public double getMaxTemperature() {
        return temperatures.getMaxTemperature();
    }

    public double getMorningTemperature() {
        return temperatures.getMorningTemperature();
    }

    public double getDayTemperature() {
        return temperatures.getDayTemperature();
    }

    public double getEveningTemperature() {
        return temperatures.getEveningTemperature();
    }

    public double getNightTemperature() {
        return temperatures.getNightTemperature();
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getConditionId() {
        return condition.get(0).getId();
    }

    public String getConditionIconId() {
        return condition.get(0).getIconId();
    }
}