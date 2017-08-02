package com.example.julia.weatherguide.interactors;

import io.reactivex.Completable;

public interface MainViewInteractor {

    Completable saveLocation(float longitude, float latitude, String cityName);

}
