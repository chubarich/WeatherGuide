package com.example.julia.weatherguide.data.contracts.local.location;


public class LocationContract {

    private LocationContract() {
    }

    // ------------------------------------- constants --------------------------------------------

    public static final String TABLE_NAME = "locations";


    public static final String COLUMN_NAME_ID = "_id";

    public static final String COLUMN_NAME_NAME = "name";


    public static final String COLUMN_NAME_LATITUDE = "latitude";

    public static final String COLUMN_NAME_LONGITUDE = "longitude";



    public static String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
        + COLUMN_NAME_NAME + " TEXT NOT NULL, "

        + COLUMN_NAME_LATITUDE + " REAL NOT NULL, "
        + COLUMN_NAME_LONGITUDE + " REAL NOT NULL, "

        + "UNIQUE(" + COLUMN_NAME_LATITUDE + ", " + COLUMN_NAME_LONGITUDE + ") ON CONFLICT REPLACE"
        + ");"; // pair "original text" + "language" is unique

    public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
