package com.example.julia.weatherguide.di.modules.singleton;

import android.content.Context;

import com.example.julia.weatherguide.BuildConfig;
import com.example.julia.weatherguide.data.data_services.location.LocalLocationService;
import com.example.julia.weatherguide.data.data_services.location.StorIOLocationService;
import com.example.julia.weatherguide.data.data_services.weather.LocalWeatherService;
import com.example.julia.weatherguide.data.data_services.weather.StorIOWeatherService;
import com.example.julia.weatherguide.data.database.StorIOFactory;
import com.example.julia.weatherguide.data.database.StorIOWeatherHelper;
import com.example.julia.weatherguide.di.qualifiers.DatabaseName;
import com.example.julia.weatherguide.di.qualifiers.DatabaseVersion;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class DatabaseModule {

    @Provides
    @Singleton
    @DatabaseName
    String provideDatabaseName() {
        return BuildConfig.DATABASE_NAME;
    }

    @Provides
    @Singleton
    @DatabaseVersion
    Integer provideDatabaseVersion() {
        return BuildConfig.DATABASE_VERSION;
    }

    @Provides
    @Singleton
    StorIOSQLite provideStorIOSQLite(Context context, @DatabaseName String databaseName,
                                     @DatabaseVersion Integer databaseVersion) {
        return StorIOFactory.create(new StorIOWeatherHelper(context, databaseName, databaseVersion));
    }

    @Provides
    @Singleton
    LocalWeatherService provideLocalWeatherService(StorIOSQLite storIOSQLite) {
        return new StorIOWeatherService(storIOSQLite);
    }

    @Provides
    @Singleton
    LocalLocationService provideLocalLocationService(StorIOSQLite storIOSQLite){
        return new StorIOLocationService(storIOSQLite);
    }

}
