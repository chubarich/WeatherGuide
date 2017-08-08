package com.example.julia.weatherguide.data.database;

import android.database.sqlite.SQLiteOpenHelper;

import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeather;
import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeatherStorIOSQLiteDeleteResolver;
import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeatherStorIOSQLiteGetResolver;
import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeatherStorIOSQLitePutResolver;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocationStorIOSQLiteDeleteResolver;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocationStorIOSQLiteGetResolver;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocationStorIOSQLitePutResolver;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPrediction;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPredictionStorIOSQLiteDeleteResolver;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPredictionStorIOSQLiteGetResolver;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPredictionStorIOSQLitePutResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;


public class StorIOFactory {

    private StorIOFactory() {
    }

    public static StorIOSQLite create(SQLiteOpenHelper sqLiteOpenHelper) {
        return DefaultStorIOSQLite.builder()
            .sqliteOpenHelper(sqLiteOpenHelper)
            .addTypeMapping(DatabaseCurrentWeather.class, SQLiteTypeMapping.<DatabaseCurrentWeather>builder()
                .putResolver(new DatabaseCurrentWeatherStorIOSQLitePutResolver())
                .getResolver(new DatabaseCurrentWeatherStorIOSQLiteGetResolver())
                .deleteResolver(new DatabaseCurrentWeatherStorIOSQLiteDeleteResolver())
                .build()
            )
            .addTypeMapping(DatabaseWeatherPrediction.class, SQLiteTypeMapping.<DatabaseWeatherPrediction>builder()
                .putResolver(new DatabaseWeatherPredictionStorIOSQLitePutResolver())
                .getResolver(new DatabaseWeatherPredictionStorIOSQLiteGetResolver())
                .deleteResolver(new DatabaseWeatherPredictionStorIOSQLiteDeleteResolver())
                .build()
            )
            .addTypeMapping(DatabaseLocation.class, SQLiteTypeMapping.<DatabaseLocation>builder()
                .putResolver(new DatabaseLocationStorIOSQLitePutResolver())
                .getResolver(new DatabaseLocationStorIOSQLiteGetResolver())
                .deleteResolver(new DatabaseLocationStorIOSQLiteDeleteResolver())
                .build()
            )
            .build();
    }

}
