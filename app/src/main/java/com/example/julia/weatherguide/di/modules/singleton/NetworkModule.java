package com.example.julia.weatherguide.di.modules.singleton;


import com.example.julia.weatherguide.BuildConfig;
import com.example.julia.weatherguide.data.constants.GoogleMapsEndpoints;
import com.example.julia.weatherguide.data.constants.OpenWeatherMapEndpoints;
import com.example.julia.weatherguide.data.data_services.location.GoogleMapsService;
import com.example.julia.weatherguide.data.data_services.location.NetworkLocationService;
import com.example.julia.weatherguide.data.data_services.weather.NetworkWeatherService;
import com.example.julia.weatherguide.data.data_services.weather.OpenWeatherMapService;
import com.example.julia.weatherguide.di.qualifiers.GoogleMaps;
import com.example.julia.weatherguide.di.qualifiers.OpenWeatherMap;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;


@Module
public class NetworkModule {

    @Provides
    @Singleton
    @OpenWeatherMap
    String provideOpenWeatherMapApiKey() {
        return BuildConfig.OPEN_WEATHER_MAP_API_KEY;
    }

    @Provides
    @Singleton
    @GoogleMaps
    String provideGoogleMapsApiKey() {
        return BuildConfig.GOOGLE_MAPS_API_KEY;
    }

    @Provides
    @Singleton
    @OpenWeatherMap
    OkHttpClient provideOpenWeatherMapOkHttpClient(@OpenWeatherMap String apiKey) {
        return new OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(chain ->
                chain.proceed(chain.request()
                    .newBuilder()
                    .url(chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter(OpenWeatherMapEndpoints.KEY_SECURITY_KEY, apiKey)
                        .build())
                    .build()
                )
            ).build();
    }

    @Provides
    @Singleton
    @GoogleMaps
    OkHttpClient provideGoogleMapsOkHttpClient(@GoogleMaps String apiKey) {
        return new OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(chain ->
                chain.proceed(chain.request()
                    .newBuilder()
                    .url(chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter(GoogleMapsEndpoints.KEY_SECURITY_KEY, apiKey)
                        .build())
                    .build()
                )
            ).build();
    }

    @Provides
    @Singleton
    NetworkWeatherService provideNetworkWeatherService(@OpenWeatherMap OkHttpClient okHttpClient) {
        return new OpenWeatherMapService(okHttpClient);
    }

    @Provides
    @Singleton
    NetworkLocationService provideNetworkLocationService(@GoogleMaps OkHttpClient okHttpClient) {
        return new GoogleMapsService(okHttpClient);
    }

}
