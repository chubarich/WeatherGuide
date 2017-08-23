package com.example.julia.weatherguide.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.julia.weatherguide.data.contracts.local.weather.CurrentWeatherContract;
import com.example.julia.weatherguide.data.contracts.local.location.LocationContract;
import com.example.julia.weatherguide.data.contracts.local.weather.WeatherPredictionContract;


public class StorIOWeatherHelper extends SQLiteOpenHelper {

    public StorIOWeatherHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LocationContract.SQL_CREATE_TABLE);
        db.execSQL(CurrentWeatherContract.SQL_CREATE_TABLE);
        db.execSQL(WeatherPredictionContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LocationContract.SQL_DELETE_TABLE);
        db.execSQL(CurrentWeatherContract.SQL_DELETE_TABLE);
        db.execSQL(WeatherPredictionContract.SQL_DELETE_TABLE);

        onCreate(db);
    }
}
