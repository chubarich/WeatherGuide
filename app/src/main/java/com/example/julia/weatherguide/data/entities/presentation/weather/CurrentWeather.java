package com.example.julia.weatherguide.data.entities.presentation.weather;

import android.support.annotation.DrawableRes;

import com.example.julia.weatherguide.utils.Preconditions;


public class CurrentWeather {

    // meta
    private String datetimeOfUpdate;

    // condition
    private int conditionId;
    private String conditionDescription;
    @DrawableRes
    private int conditionIconResource;

    // temperature
    private double mainTemperature;

    //wind
    private double windSpeed;
    private double windAngle;

    // other
    private int humidity;
    private double pressure;
    private int cloudiness;


    private CurrentWeather(Builder builder) {
        datetimeOfUpdate = builder.datetimeOfUpdate;
        conditionId = builder.conditionId;
        conditionDescription = builder.conditionDescription;
        conditionIconResource = builder.conditionIconResource;
        mainTemperature = builder.mainTemperature;
        windSpeed = builder.windSpeed;
        windAngle = builder.windAngle;
        humidity = builder.humidity;
        pressure = builder.pressure;
        cloudiness = builder.cloudiness;
    }


    public static final class Builder {
        private String datetimeOfUpdate;
        private Integer conditionId;
        private String conditionDescription;
        private Integer conditionIconResource;
        private Double mainTemperature;
        private Double windSpeed;
        private Double windAngle;
        private Integer humidity;
        private Double pressure;
        private Integer cloudiness;

        public Builder() {
        }

        public Builder datetimeOfUpdate(String val) {
            datetimeOfUpdate = val;
            return this;
        }

        public Builder conditionId(int val) {
            conditionId = val;
            return this;
        }

        public Builder conditionDescription(String val) {
            conditionDescription = val;
            return this;
        }

        public Builder conditionIconResource(int val) {
            conditionIconResource = val;
            return this;
        }

        public Builder mainTemperature(double val) {
            mainTemperature = val;
            return this;
        }

        public Builder windSpeed(double val) {
            windSpeed = val;
            return this;
        }

        public Builder windAngle(double val) {
            windAngle = val;
            return this;
        }

        public Builder humidity(int val) {
            humidity = val;
            return this;
        }

        public Builder pressure(double val) {
            pressure = val;
            return this;
        }

        public Builder cloudiness(int val) {
            cloudiness = val;
            return this;
        }

        public CurrentWeather build() {
            Preconditions.nonNull(datetimeOfUpdate, conditionId, conditionDescription,
                conditionIconResource, mainTemperature, windSpeed, windAngle,
                humidity, pressure, cloudiness);
            return new CurrentWeather(this);
        }
    }
}