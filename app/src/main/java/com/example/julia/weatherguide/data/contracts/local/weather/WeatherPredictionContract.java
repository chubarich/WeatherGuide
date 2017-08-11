package com.example.julia.weatherguide.data.contracts.local.weather;


public class WeatherPredictionContract {

    private WeatherPredictionContract() {
    }

    // -------------------------------------- constants -------------------------------------------

    public static final String TABLE_NAME = "prediction_weathers";

    // id
    public static final String COLUMN_NAME_ID = "_id";

    public static final String COLUMN_NAME_LOCATION_ID = "location_id";

    // meta
    public static final String COLUMN_NAME_DATE = "date";

    // condition
    public static final String COLUMN_NAME_CONDITION_ID = "condition_id";

    public static final String COLUMN_NAME_CONDITION_ICON_ID = "condition_icon_id";

    // temperature (in kelvin)
    public static final String COLUMN_NAME_MIN_TEMPERATURE = "min_temperature";

    public static final String COLUMN_NAME_MAX_TEMPERATURE = "max_temperature";

    public static final String COLUMN_NAME_MORNING_TEMPERATURE = "morning_temperature";

    public static final String COLUMN_NAME_DAY_TEMPERATURE = "day_temperature";

    public static final String COLUMN_NAME_EVENING_TEMPERATURE = "evening_temperature";

    public static final String COLUMN_NAME_NIGHT_TEMPERATURE = "night_temperature";

    // wind
    public static final String COLUMN_NAME_WIND_SPEED = "wind_speed";

    public static final String COLUMN_NAME_WIND_ANGLE = "wind_angle";

    // other
    public static final String COLUMN_NAME_HUMIDITY = "humidity";

    public static final String COLUMN_NAME_PRESSURE = "pressure";

    public static final String COLUMN_NAME_CLOUDINESS = "cloudiness";



    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
        + COLUMN_NAME_LOCATION_ID + " INTEGER NON NULL, "

        + COLUMN_NAME_DATE + " STRING NON NULL, "

        + COLUMN_NAME_CONDITION_ID + " INTEGER NON NULL, "
        + COLUMN_NAME_CONDITION_ICON_ID + " TEXT NON NULL, "

        + COLUMN_NAME_MIN_TEMPERATURE + " REAL NON NULL, "
        + COLUMN_NAME_MAX_TEMPERATURE + " REAL NON NULL, "
        + COLUMN_NAME_MORNING_TEMPERATURE + " REAL NON NULL, "
        + COLUMN_NAME_DAY_TEMPERATURE + " REAL NON NULL, "
        + COLUMN_NAME_EVENING_TEMPERATURE + " REAL NON NULL, "
        + COLUMN_NAME_NIGHT_TEMPERATURE + " REAL NON NULL, "

        + COLUMN_NAME_WIND_SPEED + " REAL NON NULL, "
        + COLUMN_NAME_WIND_ANGLE + " REAL NON NULL, "

        + COLUMN_NAME_HUMIDITY + " INTEGER NON NULL, "
        + COLUMN_NAME_PRESSURE + " REAL NON NULL, "
        + COLUMN_NAME_CLOUDINESS + " INTEGER NON NULL, "

        + "UNIQUE(" + COLUMN_NAME_LOCATION_ID + ", " + COLUMN_NAME_DATE + ") ON CONFLICT REPLACE"
        + ");";

    public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
