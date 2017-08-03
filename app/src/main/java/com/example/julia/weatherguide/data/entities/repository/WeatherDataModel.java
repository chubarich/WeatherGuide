package com.example.julia.weatherguide.data.entities.repository;


public class WeatherDataModel {

    private String locationName;

    private String currentTemperature;

    private String weatherDescription;

    private String iconId;

    private int humidity;


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

    public void setCurrentTemperature(String temp) {
        currentTemperature = temp;
    }

    public String getCurrentTemperature() {
        return currentTemperature;
    }

    public void setWeatherDescription(String description) {
        weatherDescription = description;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setIconId(String id) {
        iconId = id;
    }

    public String getIconId() {
        return iconId;
    }

}
