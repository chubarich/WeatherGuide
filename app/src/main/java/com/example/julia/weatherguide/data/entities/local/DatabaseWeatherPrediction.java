package com.example.julia.weatherguide.data.entities.local;

import android.support.annotation.Nullable;

import com.example.julia.weatherguide.utils.Preconditions;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import static com.example.julia.weatherguide.data.contracts.local.weather.WeatherPredictionContract.*;

@StorIOSQLiteType(table = TABLE_NAME)
public class DatabaseWeatherPrediction {

    // id
    @StorIOSQLiteColumn(name = COLUMN_NAME_ID, key = true)
    @Nullable
    Long id;

    @StorIOSQLiteColumn(name = COLUMN_NAME_LOCATION_ID)
    long locationId;

    // meta
    @StorIOSQLiteColumn(name = COLUMN_NAME_DATE)
    String date;

    // condition
    @StorIOSQLiteColumn(name = COLUMN_NAME_CONDITION_ID)
    int conditionId;

    @StorIOSQLiteColumn(name = COLUMN_NAME_CONDITION_ICON_ID)
    String conditionIconId;

    // temperature
    @StorIOSQLiteColumn(name = COLUMN_NAME_MIN_TEMPERATURE)
    double minTemperature;

    @StorIOSQLiteColumn(name = COLUMN_NAME_MAX_TEMPERATURE)
    double maxTemperature;

    @StorIOSQLiteColumn(name = COLUMN_NAME_MORNING_TEMPERATURE)
    double morningTemperature;

    @StorIOSQLiteColumn(name = COLUMN_NAME_DAY_TEMPERATURE)
    double dayTemperature;

    @StorIOSQLiteColumn(name = COLUMN_NAME_EVENING_TEMPERATURE)
    double eveningTemperature;

    @StorIOSQLiteColumn(name = COLUMN_NAME_NIGHT_TEMPERATURE)
    double nightTemperature;

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


    DatabaseWeatherPrediction() {
    }

    private DatabaseWeatherPrediction(Builder builder) {
        id = null;
        locationId = builder.locationId;
        date = builder.date;
        conditionId = builder.conditionId;
        conditionIconId = builder.conditionIconId;
        minTemperature = builder.minTemperature;
        maxTemperature = builder.maxTemperature;
        morningTemperature = builder.morningTemperature;
        dayTemperature = builder.dayTemperature;
        eveningTemperature = builder.eveningTemperature;
        nightTemperature = builder.nightTemperature;
        windSpeed = builder.windSpeed;
        windAngle = builder.windAngle;
        humidity = builder.humidity;
        pressure = builder.pressure;
        cloudiness = builder.cloudiness;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public long getLocationId() {
        return locationId;
    }

    public String getDate() {
        return date;
    }

    public int getConditionId() {
        return conditionId;
    }

    public String getConditionIconId() {
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
        private Long locationId;
        private String date;
        private Integer conditionId;
        private String conditionIconId;
        private Double minTemperature;
        private Double maxTemperature;
        private Double morningTemperature;
        private Double dayTemperature;
        private Double eveningTemperature;
        private Double nightTemperature;
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

        public Builder date(String val) {
            date = val;
            return this;
        }

        public Builder conditionId(int val) {
            conditionId = val;
            return this;
        }

        public Builder conditionIconId(String val) {
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

        public DatabaseWeatherPrediction build() {
            Preconditions.nonNull(locationId, date, conditionId, conditionIconId,
                minTemperature, maxTemperature, morningTemperature, dayTemperature,
                eveningTemperature, nightTemperature, windSpeed, windAngle, humidity,
                pressure, cloudiness);
            return new DatabaseWeatherPrediction(this);
        }
    }
}
