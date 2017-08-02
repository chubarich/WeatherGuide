package com.example.julia.weatherguide.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.julia.weatherguide.di.qualifiers.SharedPreferencesName;
import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceServiceImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Provides
    @ScreenScope
    @SharedPreferencesName
    String provideSharedPreferencesName() {
        return "current_weather";
    }

    @Provides
    @ScreenScope
    SharedPreferences provideSharedPreferences(@SharedPreferencesName String sharedPreferencesName,
                                               Context context) {
        return context.getSharedPreferences(sharedPreferencesName, 0);
    }

    @Provides
    @ScreenScope
    SharedPreferenceService provideSharedPreferenceService(SharedPreferences sharedPreferences) {
        return new SharedPreferenceServiceImpl(sharedPreferences);
    }

}
