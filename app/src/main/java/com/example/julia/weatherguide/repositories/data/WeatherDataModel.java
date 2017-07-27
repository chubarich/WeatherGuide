package com.example.julia.weatherguide.repositories.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public class WeatherDataModel {

    private String locationName;

    private String currentTemperature;

    private String weatherDescription;

    private String iconId;

    private int humidity;

    private Bitmap icon;


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setCurrentTemperature(@NonNull String temp) {
        currentTemperature = temp;
    }

    public String getCurrentTemperature() {
        return currentTemperature;
    }

    public void setWeatherDescription(@NonNull String description) {
        weatherDescription = description;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setIconId(@NonNull String id) {
        iconId = id;
    }

    public String getIconId() {
        return iconId;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

}
