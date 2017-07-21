package com.example.julia.weatherguide.ui.base;

import android.support.annotation.NonNull;

/**
 * Created by julia on 16.07.17.
 */

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {

    V relatedView;

    public void attachView(@NonNull  V view) {
        relatedView = view;
    }

    public void detachView() {
        relatedView = null;
    }

    public boolean isViewAttached() {
        if (relatedView != null) {
            return true;
        }
        return false;
    }

    public V getView() {
        return relatedView;

    }


}
