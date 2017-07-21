package com.example.julia.weatherguide.ui.base;

import android.support.annotation.NonNull;

/**
 * Created by julia on 21.07.17.
 */

public interface BasePresenter<V extends BaseView> {

    void attachView(@NonNull V view);
    void detachView();
    boolean isViewAttached();
    V getView();
}
