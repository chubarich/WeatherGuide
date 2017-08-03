package com.example.julia.weatherguide.presentation.settings;

import com.example.julia.weatherguide.presentation.base.view.BaseView;

public interface SettingsView extends BaseView {

    void showNumberFormatError();

    void showPickTimeSuccess();

    void showLocationNotPickedError();

}
