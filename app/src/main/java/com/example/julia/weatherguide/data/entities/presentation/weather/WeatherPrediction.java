package com.example.julia.weatherguide.data.entities.presentation.weather;

import com.example.julia.weatherguide.utils.Preconditions;

public class WeatherPrediction {

    //meta
    private final String conditionDescription;
    private final String conditionIconName;
    // temperature
    private final double minTemperature;
    private final double maxTemperature;
    private final double morningTemperature;
    private final double dayTemperature;
    private final double eveningTemperature;
    private final double nightTemperatureInKelvin;
    // wind
    private final double windSpeed;
    private final double windAngle;
    // other
    private final int humidity;
    private final double pressure;
    private final int cloudiness;

    private WeatherPrediction(Builder builder) {
        conditionDescription = builder.conditionDescription;
        conditionIconName = builder.conditionIconName;
        minTemperature = builder.minTemperature;
        maxTemperature = builder.maxTemperature;
        morningTemperature = builder.morningTemperature;
        dayTemperature = builder.dayTemperature;
        eveningTemperature = builder.eveningTemperature;
        nightTemperatureInKelvin = builder.nightTemperatureInKelvin;
        windSpeed = builder.windSpeed;
        windAngle = builder.windAngle;
        humidity = builder.humidity;
        pressure = builder.pressure;
        cloudiness = builder.cloudiness;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public String getConditionIconName() {
        return conditionIconName;
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

    public double getEveningTemperature() {
        return eveningTemperature;
    }

    public double getNightTemperatureInKelvin() {
        return nightTemperatureInKelvin;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindAngle() {
        return windAngle;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public static final class Builder {
        private String date;
        private String conditionDescription;
        private String conditionIconName;
        private Double minTemperature;
        private Double maxTemperature;
        private Double morningTemperature;
        private Double dayTemperature;
        private Double eveningTemperature;
        private Double nightTemperatureInKelvin;
        private Double windSpeed;
        private Double windAngle;
        private Integer humidity;
        private Double pressure;
        private Integer cloudiness;

        public Builder() {
        }

        public Builder date(String val) {
            date = val;
            return this;
        }

        public Builder conditionDescription(String val) {
            conditionDescription = val;
            return this;
        }

        public Builder conditionIconName(String val) {
            conditionIconName = val;
            return this;
        }

        public Builder minTemperature(double val) {
            minTemperature = val;
            return this;
        }

        public Builder maxTemperature(double val) {
            maxTemperature = val;
            return this;
        }

        public Builder morningTemperature(double val) {
            morningTemperature = val;
            return this;
        }

        public Builder dayTemperature(double val) {
            dayTemperature = val;
            return this;
        }

        public Builder eveningTemperature(double val) {
            eveningTemperature = val;
            return this;
        }

        public Builder nightTemperatureInKelvin(double val) {
            nightTemperatureInKelvin = val;
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

        public WeatherPrediction build() {
            Preconditions.nonNull(date, conditionDescription, conditionIconName, minTemperature,
                maxTemperature, morningTemperature, dayTemperature, eveningTemperature,
                nightTemperatureInKelvin, windSpeed, windAngle, humidity, pressure, cloudiness);
            return new WeatherPrediction(this);
        }
    }


}