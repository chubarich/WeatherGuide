package com.example.julia.weatherguide.data.entities.presentation.weather;

import com.example.julia.weatherguide.utils.Preconditions;

public class WeatherPrediction {

    //meta
    private final String date;
    private final String conditionDescription;
    private final int conditionIconId;
    // temperature
    private final double minTemperature;
    private final double maxTemperature;
    private final double morningTemperature;
    private final double dayTemperature;
    private final double eveningTemperature;
    private final double nightTemperature;
    // wind
    private final String windSummary;
    // other
    private final int humidity;
    private final String pressureSummary;
    private final int cloudiness;

    private WeatherPrediction(Builder builder) {
        date = builder.date;
        conditionDescription = builder.conditionDescription;
        conditionIconId = builder.conditionIconId;
        minTemperature = builder.minTemperature;
        maxTemperature = builder.maxTemperature;
        morningTemperature = builder.morningTemperature;
        dayTemperature = builder.dayTemperature;
        eveningTemperature = builder.eveningTemperature;
        nightTemperature = builder.nightTemperature;
        windSummary = builder.windSummary;
        humidity = builder.humidity;
        pressureSummary = builder.pressureSummary;
        cloudiness = builder.cloudiness;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public int getConditionIconId() {
        return conditionIconId;
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

    public double getNightTemperature() {
        return nightTemperature;
    }

    public String getWindSummary() {
        return windSummary;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getPressureSummary() {
        return pressureSummary;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public String getDate() {
        return date;
    }

    public static final class Builder {
        private String date;
        private String conditionDescription;
        private Integer conditionIconId;
        private Double minTemperature;
        private Double maxTemperature;
        private Double morningTemperature;
        private Double dayTemperature;
        private Double eveningTemperature;
        private Double nightTemperature;
        private String windSummary;
        private Integer humidity;
        private String pressureSummary;
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

        public Builder conditionIconId(Integer val) {
            conditionIconId = val;
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

        public Builder nightTemperature(double val) {
            nightTemperature = val;
            return this;
        }

        public Builder windSummary(String val) {
            windSummary = val;
            return this;
        }

        public Builder humidity(int val) {
            humidity = val;
            return this;
        }

        public Builder pressure(String val) {
            pressureSummary = val;
            return this;
        }

        public Builder cloudiness(int val) {
            cloudiness = val;
            return this;
        }

        public WeatherPrediction build() {
            Preconditions.nonNull(date, conditionDescription, conditionIconId, minTemperature,
                maxTemperature, morningTemperature, dayTemperature, eveningTemperature,
                nightTemperature, windSummary, humidity, pressureSummary, cloudiness);
            return new WeatherPrediction(this);
        }
    }


}