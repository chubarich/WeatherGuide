package com.example.julia.weatherguide.data.entities.local;

import com.example.julia.weatherguide.utils.Preconditions;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import static com.example.julia.weatherguide.data.contracts.local.weather.CurrentWeatherContract.*;


@StorIOSQLiteType(table = TABLE_NAME)
public class DatabaseCurrentWeather {

    // id
    @StorIOSQLiteColumn(name = COLUMN_NAME_ID, key = true)
    Long id;

    @StorIOSQLiteColumn(name = COLUMN_NAME_LOCATION_ID)
    long locationId;

    // meta
    @StorIOSQLiteColumn(name = COLUMN_NAME_TIMESTAMP_OF_UPDATE)
    long timestampOfUpdate;

    // condition
    @StorIOSQLiteColumn(name = COLUMN_NAME_CONDITION_ID)
    int conditionId;

    @StorIOSQLiteColumn(name = COLUMN_NAME_CONDITION_ICON_NAME)
    String conditionIconName;

    // temperature
    @StorIOSQLiteColumn(name = COLUMN_NAME_MAIN_TEMPERATURE)
    double mainTemperature;

    // wind
    @StorIOSQLiteColumn(name = COLUMN_NAME_WIND_SPEED)
    double windSpeed;

    @StorIOSQLiteColumn(name = COLUMN_NAME_WIND_ANGLE)
    double windAngle;

    // other
    @StorIOSQLiteColumn(name = COLUMN_NAME_HUMIDITY)
    int humidity;

    @StorIOSQLiteColumn(name = COLUMN_NAME_PRESSURE)
    double pressure;

    @StorIOSQLiteColumn(name = COLUMN_NAME_CLOUDINESS)
    int cloudiness;


    DatabaseCurrentWeather() {
    }

    public Long getId() {
        return id;
    }

    public long getLocationId() {
        return locationId;
    }

    public long getTimestampOfUpdate() {
        return timestampOfUpdate;
    }

    public int getConditionId() {
        return conditionId;
    }

    public String getConditionIconName() {
        return conditionIconName;
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

    private DatabaseCurrentWeather(Builder builder) {
        id = null;
        locationId = builder.locationId;
        timestampOfUpdate = builder.timestampOfUpdate;
        conditionId = builder.conditionId;
        conditionIconName = builder.conditionIconName;
        mainTemperature = builder.mainTemperature;
        windSpeed = builder.windSpeed;
        windAngle = builder.windAngle;
        humidity = builder.humidity;
        pressure = builder.pressure;
        cloudiness = builder.cloudiness;
    }


    public double getTemperature() {
        return mainTemperature;
    }


    public static final class Builder {
        private Long locationId;
        private Long timestampOfUpdate;
        private Integer conditionId;
        private String conditionIconName;
        private Double mainTemperature;
        private Double windSpeed;
        private Double windAngle;
        private Integer humidity;
        private Double pressure;
        private Integer cloudiness;

        public Builder() {
        }

        public Builder locationId(long val) {
            locationId = val;
            return this;
        }

        public Builder timestampOfUpdate(long val) {
            timestampOfUpdate = val;
            return this;
        }

        public Builder conditionId(int val) {
            conditionId = val;
            return this;
        }

        public Builder conditionIconName(String val) {
            conditionIconName = val;
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

        public DatabaseCurrentWeather build() {
            Preconditions.nonNull(locationId, timestampOfUpdate, conditionId, conditionIconName,
                mainTemperature, windSpeed, windAngle, humidity, pressure, cloudiness);
            return new DatabaseCurrentWeather(this);
        }
    }
}
