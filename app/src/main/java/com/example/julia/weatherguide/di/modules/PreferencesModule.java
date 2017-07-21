package com.example.julia.weatherguide.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by julia on 20.07.17.
 */
@Module
public class PreferencesModule {


    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

   /* @Provides
    @Singleton
    public SharedPreferences provideCustomSharedPreferences (@NonNull Context context, @NonNull String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }*/
}
