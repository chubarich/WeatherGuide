package com.example.julia.weatherguide.presentation.base.presenter;

import com.example.julia.weatherguide.presentation.base.view.BaseView;

public interface PresenterFactory<P extends BasePresenter<V>, V extends BaseView> {

    P create();

}