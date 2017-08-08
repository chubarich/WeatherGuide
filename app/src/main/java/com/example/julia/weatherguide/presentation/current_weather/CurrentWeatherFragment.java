package com.example.julia.weatherguide.presentation.current_weather;

import com.example.julia.weatherguide.data.entities.presentation.weather.CurrentWeather;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.view.BaseFragment;


public class CurrentWeatherFragment extends BaseFragment<CurrentWeatherPresenter, CurrentWeatherView>
    implements CurrentWeatherView {

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void showCityNotPicked() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showData(CurrentWeather data) {

    }

    // ------------------------------------- BaseFragment -----------------------------------------

    @Override
    protected CurrentWeatherView getViewInterface() {
        return null;
    }

    @Override
    protected PresenterFactory<CurrentWeatherPresenter, CurrentWeatherView> getPresenterFactory() {
        return null;
    }

    @Override
    protected int getFragmentId() {
        return 0;
    }

    @Override
    protected int getLayoutRes() {
        return 0;
    }
}
