package com.example.julia.weatherguide.di.modules;

import android.content.Context;

import com.example.julia.weatherguide.di.scopes.ScreenScope;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceServiceImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Provides
    @ScreenScope
    SharedPreferenceService provideSharedPreferenceService(Context context) {
        return new SharedPreferenceServiceImpl(context);
    }

}
