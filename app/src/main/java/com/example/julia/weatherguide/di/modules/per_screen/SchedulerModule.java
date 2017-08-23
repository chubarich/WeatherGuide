package com.example.julia.weatherguide.di.modules.per_screen;

import com.example.julia.weatherguide.di.qualifiers.PostExecutionScheduler;
import com.example.julia.weatherguide.di.qualifiers.WorkerScheduler;
import com.example.julia.weatherguide.di.scopes.PerScreen;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class SchedulerModule {

    @Provides
    @PerScreen
    @WorkerScheduler
    Scheduler provideWorkerScheduler() {
        return Schedulers.io();
    }

    @Provides
    @PerScreen
    @PostExecutionScheduler
    Scheduler provideUiScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
