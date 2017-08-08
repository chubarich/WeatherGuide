package com.example.julia.weatherguide.data.entities.presentation.weather;

import android.support.annotation.DrawableRes;


public class CurrentWeather {

    // meta
    private String datetimeOfUpdate;

    // condition
    private int conditionId;
    private String conditionDescription;
    @DrawableRes
    private int conditionIconResource;

    // temperature
    private double mainTemperatureInKelvin;

    //wind
    private double windSpeed;
    private double windAngle;

    // other
    private int humidity;
    private double pressure;
    private int cloudiness;



}