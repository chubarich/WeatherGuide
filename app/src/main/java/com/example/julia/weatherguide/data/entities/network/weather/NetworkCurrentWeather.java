package com.example.julia.weatherguide.data.entities.network.weather;

import com.example.julia.weatherguide.utils.Preconditions;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class NetworkCurrentWeather {

    @SerializedName("weather")
    private List<NetworkCondition> conditions;

    @SerializedName("main")
    private NetworkMain main;

    @SerializedName("wind")
    private NetworkWind wind;

    @SerializedName("clouds")
    private NetworkClouds clouds;

    @SerializedName("cod")
    private int code;

    public NetworkCurrentWeather(NetworkCondition condition, NetworkMain main,
                                 NetworkWind wind, NetworkClouds clouds) {
        Preconditions.nonNull(condition, main, wind, clouds);

        List<NetworkCondition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.conditions = conditions;
        this.main = main;
        this.wind = wind;
        this.clouds = clouds;
    }


    public double getTemperature() {
        return main.getTemp();
    }

    public int getConditionId() {
        return conditions.get(0).getId();
    }

    public String getConditionIconName() {
        return conditions.get(0).getIconId();
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
