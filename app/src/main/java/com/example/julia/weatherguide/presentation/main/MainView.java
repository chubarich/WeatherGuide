package com.example.julia.weatherguide.presentation.main;

import com.example.julia.weatherguide.presentation.base.view.BaseView;

public interface MainView extends BaseView {

    void showLocationChangedSuccess();

    void showLocationChangedException();

}
