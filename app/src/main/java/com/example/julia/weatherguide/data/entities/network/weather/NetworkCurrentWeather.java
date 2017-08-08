package com.example.julia.weatherguide.data.entities.network.weather;

import com.google.gson.annotations.SerializedName;
import java.util.List;


public class NetworkCurrentWeather {

    @SerializedName("condition")
    private List<NetworkCondition> condition;

    @SerializedName("main")
    private NetworkMain main;

    @SerializedName("wind")
    private NetworkWind wind;

    @SerializedName("clouds")
    private NetworkClouds clouds;

    @SerializedName("cod")
    private int code;


    public double getTemperature() {
        return main.getTemp();
    }

    public int getConditionId() {
        return condition.get(0).getId();
    }

    public String getConditionIconId() {
        return condition.get(0).getIconId();
    }

    public double getWindSpeed() {
        return wind.getSpeed();
    }

    public double getWindAngle() {
        return wind.getAngle();
    }

    public double getHumidity() {
        return main.getHumidity();
    }

    public double getPressure() {
        return main.getPressure();
    }

    public double getCloudiness() {
        return clouds.getCloudiness();
    }

    public int getCode() {
        return code;
    }
}
