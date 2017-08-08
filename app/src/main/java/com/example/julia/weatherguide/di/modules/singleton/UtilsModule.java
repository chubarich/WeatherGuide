package com.example.julia.weatherguide.di.modules.singleton;

import com.example.julia.weatherguide.data.converters.location.LocationConverter;
import com.example.julia.weatherguide.data.converters.location.LocationConverterPlain;
import com.example.julia.weatherguide.data.converters.weather.SharedPreferencesWeatherConverter;
import com.example.julia.weatherguide.data.converters.weather.WeatherConverter;
import com.example.julia.weatherguide.data.helpers.DatetimeHelper;
import com.example.julia.weatherguide.data.helpers.DatetimeHelperPlain;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class UtilsModule {

    @Provides
    @Singleton
    WeatherConverter provideWeatherConverter() {
        return new SharedPreferencesWeatherConverter();
    }

    @Provides
    @Singleton
    DatetimeHelper provideDatetimeHepler() {
        return new DatetimeHelperPlain();
    }

    @Provides
    @Singleton
    LocationConverter provideLocationConverter() {
        return new LocationConverterPlain();
    }

}
