package com.example.julia.weatherguide.presentation.base.presenter;

import android.support.annotation.NonNull;

interface Presenter<V> {

    void attachView(@NonNull V view);

    void detachView();

    void destroy();

}
