package com.example.julia.weatherguide.di.modules.singleton;

import android.content.Context;
import android.content.res.Resources;

import com.example.julia.weatherguide.di.qualifiers.ApplicationPackageName;
import com.example.julia.weatherguide.utils.Preconditions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        Preconditions.nonNull(context);
        this.context = context.getApplicationContext();
    }

    @Provides
    Context provideContext() {
        return context;
    }

    @Provides
    Resources provideResources() {
        return context.getResources();
    }

    @Provides
    @ApplicationPackageName
    String provideApplicationPackageName() {
        return context.getPackageName();
    }

}
