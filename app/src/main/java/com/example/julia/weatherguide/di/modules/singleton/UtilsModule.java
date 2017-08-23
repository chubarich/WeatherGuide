package com.example.julia.weatherguide.di.modules.singleton;

import android.content.res.Resources;

import com.example.julia.weatherguide.data.converters.location.LocationConverter;
import com.example.julia.weatherguide.data.converters.location.LocationConverterPlain;
import com.example.julia.weatherguide.data.converters.weather.SharedPreferencesWeatherConverter;
import com.example.julia.weatherguide.data.converters.weather.WeatherConverter;
import com.example.julia.weatherguide.data.data_services.settings.SettingsService;
import com.example.julia.weatherguide.data.helpers.DatetimeHelper;
import com.example.julia.weatherguide.data.helpers.DatetimeHelperPlain;
import com.example.julia.weatherguide.di.qualifiers.ApplicationPackageName;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class UtilsModule {

    @Provides
    @Singleton
    DatetimeHelper provideDatetimeHelper() {
        return new DatetimeHelperPlain();
    }

    @Provides
    @Singleton
    WeatherConverter provideWeatherConverter(DatetimeHelper datetimeHelper, Resources resources,
                                             SettingsService settingsService,
                                             @ApplicationPackageName String applicationPackageName) {
        return new SharedPreferencesWeatherConverter(datetimeHelper, resources,
            settingsService, applicationPackageName);
    }

    @Provides
    @Singleton
    LocationConverter provideLocationConverter() {
        return new LocationConverterPlain();
    }

}
