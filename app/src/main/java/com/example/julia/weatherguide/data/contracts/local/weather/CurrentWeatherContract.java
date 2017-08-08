package com.example.julia.weatherguide.data.contracts.local.weather;


public class CurrentWeatherContract {


    private CurrentWeatherContract() {
    }

    // -------------------------------------- constants -------------------------------------------

    public static final String TABLE_NAME = "current_weathers";

    // id
    public static final String COLUMN_NAME_ID = "_id";

    public static final String COLUMN_NAME_LOCATION_ID = "location_id";

    // meta
    public static final String COLUMN_NAME_TIMESTAMP_OF_UPDATE = "timestamp_of_update";

    // condition
    public static final String COLUMN_NAME_CONDITION_ID = "condition_id";

    public static final String COLUMN_NAME_CONDITION_ICON_NAME = "condition_icon_name";

    // temperature (in kelvin)
    public static final String COLUMN_NAME_MAIN_TEMPERATURE = "main_temperature";

    // wind
    public static final String COLUMN_NAME_WIND_SPEED = "wind_speed";

    public static final String COLUMN_NAME_WIND_ANGLE = "wind_angle";

    // other
    public static final String COLUMN_NAME_HUMIDITY = "humidity";

    public static final String COLUMN_NAME_PRESSURE = "pressure";

    public static final String COLUMN_NAME_CLOUDINESS = "cloudiness";



    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
        + COLUMN_NAME_LOCATION_ID + " INTEGER NON NULL UNIQUE ON CONFLICT REPLACE, "
        + COLUMN_NAME_TIMESTAMP_OF_UPDATE + " TIMESTAMP NON NULL, "

        + COLUMN_NAME_CONDITION_ID + " INTEGER NON NULL, "
        + COLUMN_NAME_CONDITION_ICON_NAME + " TEXT NON NULL, "

        + COLUMN_NAME_MAIN_TEMPERATURE + " REAL NON NULL, "

        + COLUMN_NAME_WIND_SPEED + " REAL NON NULL, "
        + COLUMN_NAME_WIND_ANGLE + " REAL NON NULL, "

        + COLUMN_NAME_HUMIDITY + " INTEGER NON NULL, "
        + COLUMN_NAME_PRESSURE + " REAL NON NULL, "
        + COLUMN_NAME_CLOUDINESS + " INTEGER NON NULL "

        + ");";

    public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
