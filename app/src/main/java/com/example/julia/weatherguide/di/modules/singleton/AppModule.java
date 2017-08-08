package com.example.julia.weatherguide.di.modules.singleton;

import android.content.Context;

import com.example.julia.weatherguide.utils.Preconditions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        Preconditions.nonNull(context);
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

}
