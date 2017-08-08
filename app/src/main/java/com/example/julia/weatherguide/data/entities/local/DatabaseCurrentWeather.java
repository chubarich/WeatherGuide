package com.example.julia.weatherguide.data.entities.local;

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


    public double getMainTemperature() {
        return mainTemperature;
    }

}
