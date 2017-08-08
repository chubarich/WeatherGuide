package com.example.julia.weatherguide.data.entities.local;

import android.support.annotation.Nullable;

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

    @StorIOSQLiteColumn(name = COLUMN_NAME_CONDITION_ICON_NAME)
    String conditionIconName;

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
    double nightTemperatureInKelvin;

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


}
