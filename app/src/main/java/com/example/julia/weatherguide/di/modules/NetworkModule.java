package com.example.julia.weatherguide.di.modules;

import com.example.julia.weatherguide.BuildConfig;
import com.example.julia.weatherguide.data.contants.OpenWeatherMapEndpoints;
import com.example.julia.weatherguide.data.data_services.network.weather.NetworkWeatherService;
import com.example.julia.weatherguide.di.qualifiers.WeatherApiKey;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.data.data_services.network.weather.OpenWeatherMapService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class NetworkModule {

    @Provides
    @ScreenScope
    @WeatherApiKey
    String provideWeatherApiKey() {
        return BuildConfig.WEATHER_API_KEY;
    }

    @Provides
    @ScreenScope
    OkHttpClient provideOkHttpClient(String weatherApiKey) {
        return new OkHttpClient().newBuilder()
            .addInterceptor(chain ->
                chain.proceed(chain.request()
                    .newBuilder()
                    .url(chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter(OpenWeatherMapEndpoints.KEY_ID, weatherApiKey)
                        .build()
                    ).build()
                )
            ).build();
    }

    @Provides
    @ScreenScope
    NetworkWeatherService provideOpenWeatherMapNetworkService(String weatherApiKey,
                                                              OkHttpClient okHttpClient) {
        return new OpenWeatherMapService(weatherApiKey, okHttpClient);
    }
}
