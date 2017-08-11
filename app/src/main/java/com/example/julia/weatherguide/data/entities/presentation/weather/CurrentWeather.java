package com.example.julia.weatherguide.data.entities.presentation.weather;


import com.example.julia.weatherguide.utils.Preconditions;

public class CurrentWeather {

    // meta
    private String datetimeOfUpdate;

    // condition
    private String conditionDescription;
    private int conditionIconResource;

    // temperature
    private int mainTemperature;

    //wind
    private String windSummary;

    // other
    private int humidity;
    private String pressureSummary;
    private int cloudiness;


    private CurrentWeather(Builder builder) {
        datetimeOfUpdate = builder.datetimeOfUpdate;
        conditionDescription = builder.conditionDescription;
        conditionIconResource = builder.conditionIconResource;
        mainTemperature = builder.mainTemperature;
        windSummary = builder.windSummary;
        humidity = builder.humidity;
        pressureSummary = builder.pressureSummary;
        cloudiness = builder.cloudiness;
    }

    public String getDatetimeOfUpdate() {
        return datetimeOfUpdate;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public int getConditionIconResource() {
        return conditionIconResource;
    }

    public int getMainTemperature() {
        return mainTemperature;
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

    public static final class Builder {
        private String datetimeOfUpdate;
        private String conditionDescription;
        private Integer conditionIconResource;
        private Integer mainTemperature;
        private String windSummary;
        private Integer humidity;
        private String pressureSummary;
        private Integer cloudiness;

        public Builder() {
        }

        public Builder datetimeOfUpdate(String val) {
            datetimeOfUpdate = val;
            return this;
        }

        public Builder conditionDescription(String val) {
            conditionDescription = val;
            return this;
        }

        public Builder conditionIconId(int val) {
            conditionIconResource = val;
            return this;
        }

        public Builder mainTemperature(int val) {
            mainTemperature = val;
            return this;
        }

        public Builder windSummary(String val) {
            windSummary = val;
            return this;
        }

        public Builder humidity(Integer val) {
            humidity = val;
            return this;
        }

        public Builder pressureSummary(String val) {
            pressureSummary = val;
            return this;
        }

        public Builder cloudiness(int val) {
            cloudiness = val;
            return this;
        }

        public CurrentWeather build() {
            Preconditions.nonNull(datetimeOfUpdate, conditionDescription, conditionIconResource,
            mainTemperature, windSummary, humidity, pressureSummary, cloudiness);
            return new CurrentWeather(this);
        }
    }
}