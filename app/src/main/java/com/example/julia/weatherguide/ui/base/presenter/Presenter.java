package com.example.julia.weatherguide.ui.base.presenter;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.ui.base.view.BaseView;

interface Presenter<V> {

    void attachView(@NonNull V view);

    void detachView();

    void destroy();

}
