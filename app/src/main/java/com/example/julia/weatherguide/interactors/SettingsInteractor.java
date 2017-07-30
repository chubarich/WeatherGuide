package com.example.julia.weatherguide.interactors;

import io.reactivex.Completable;

public interface SettingsInteractor {

    Completable saveRefreshPeriod(int period);

}
