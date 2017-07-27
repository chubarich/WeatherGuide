package com.example.julia.weatherguide.di.modules;

import android.content.Context;

import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.repositories.network.NetworkService;
import com.example.julia.weatherguide.repositories.network.OpenWeatherMapNetworkService;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides
    @ScreenScope
    NetworkService provideOpenWeatherMapNetworkService(Context context) {
        return new OpenWeatherMapNetworkService(context);
    }

}
