package com.example.julia.weatherguide.di.modules.singleton;

import com.example.julia.weatherguide.data.converters.location.LocationConverter;
import com.example.julia.weatherguide.data.converters.weather.WeatherConverter;
import com.example.julia.weatherguide.data.data_services.location.LocalLocationService;
import com.example.julia.weatherguide.data.data_services.location.NetworkLocationService;
import com.example.julia.weatherguide.data.data_services.weather.LocalWeatherService;
import com.example.julia.weatherguide.data.data_services.weather.NetworkWeatherService;
import com.example.julia.weatherguide.data.helpers.DatetimeHelper;
import com.example.julia.weatherguide.data.repositories.location.GoogleMapsRepository;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.di.scopes.PerScreen;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class RepositoryModule {

    @Provides
    @Singleton
    WeatherRepository provideOpenWeatherMapRepository(DatetimeHelper datetimeHelper,
                                                      WeatherConverter weatherConverter,
                                                      LocalWeatherService localWeatherService,
                                                      NetworkWeatherService networkWeatherService) {
        return new OpenWeatherMapRepository(datetimeHelper, weatherConverter,
            localWeatherService, networkWeatherService);
    }

    @Provides
    @Singleton
    LocationRepository provideGoogleMapsRepository(LocationConverter locationConverter,
                                                   LocalLocationService localLocationService,
                                                   NetworkLocationService networkLocationService) {
        return new GoogleMapsRepository(locationConverter, localLocationService, networkLocationService);
    }

}
