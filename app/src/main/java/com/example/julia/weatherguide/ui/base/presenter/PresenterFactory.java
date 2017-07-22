package com.example.julia.weatherguide.ui.base.presenter;


import com.example.julia.weatherguide.ui.base.view.BaseView;

public interface PresenterFactory<P extends BasePresenter<V>, V extends BaseView> {

  P create();

}