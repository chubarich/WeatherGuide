package com.example.julia.weatherguide.repositories;

import io.reactivex.Observable;

/**
 * Created by julia on 16.07.17.
 */

public interface SettingsRepository {

    Observable<Long> getRefreshIntervalChange();
    void saveRefreshInterval(long interval);
}
