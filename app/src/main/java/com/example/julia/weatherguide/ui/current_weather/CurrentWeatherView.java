package com.example.julia.weatherguide.ui.current_weather;

import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;
import com.example.julia.weatherguide.ui.base.BaseView;

/**
 * Created by julia on 15.07.17.
 */

public interface CurrentWeatherView extends BaseView {

    void showLoading();
    void hideLoading();
    void showError();
    void showEmptyView();

    void showData(CurrentWeatherDataModel data);
}
