package com.example.julia.weatherguide.ui.settings;

import com.example.julia.weatherguide.ui.base.BasePresenter;

/**
 * Created by julia on 16.07.17.
 */

public interface SettingsPresenter<V extends SettingsView> extends BasePresenter<V> {

    void onRefreshPeriodChanged(String newValue);
}
