package com.example.julia.weatherguide.data.entities.network.weather;

import com.google.gson.annotations.SerializedName;

public class NetworkMain {

    @SerializedName("temp")
    private double temperature;

    @SerializedName("pressure")
    private double pressure;

    @SerializedName("humidity")
    private double humidity;

    public double getTemp() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

}
