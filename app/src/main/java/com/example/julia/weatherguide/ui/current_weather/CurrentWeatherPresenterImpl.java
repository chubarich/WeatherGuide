package com.example.julia.weatherguide.ui.current_weather;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;
import com.example.julia.weatherguide.ui.base.BasePresenterImpl;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by julia on 16.07.17.
 */

public class CurrentWeatherPresenterImpl extends BasePresenterImpl<CurrentWeatherView> implements CurrentWeatherPresenter<CurrentWeatherView> {

    @Inject
    CurrentWeatherInteractor weatherInteractor;

    private static CurrentWeatherViewState viewState = new CurrentWeatherViewState();

    public CurrentWeatherPresenterImpl() {
        WeatherGuideApplication.getDataComponent().inject(this);
    }

    @Override
    public void attachView(@NonNull CurrentWeatherView view) {
        super.attachView(view);
        if (viewState.isLoading()) {
            if (viewState.isReasonForLoadingUpdate()) {
                refreshCurrentWeather(viewState.getLocation());
            } else {
                loadCurrentWeatherForLocation(viewState.getLocation());
            }
        } else {
            if (viewState.hasData()) {
                getView().showData(viewState.getData());
            } else {
                loadCurrentWeatherForLocation(weatherInteractor.getCurrentLocation());
            }
        }
    }

    public void loadCurrentWeatherForLocation(@NonNull String location) {
        prepareViewForRefreshData(location);
        weatherInteractor.getCurrentWeatherForLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> successGetDataAction(data),
                        error -> {
                            if (isViewAttached()) {
                                if (viewState.hasData()) {
                                    errorGetDataAction(error);
                                } else {
                                    getView().hideLoading();
                                    viewState.setLoading(false);
                                    getView().showEmptyView();
                                }
                            }
                        });

    }


    public void refreshCurrentWeather(@NonNull String location) {
        prepareViewForRefreshData(location);
        viewState.setReasonForLoadingUpdate(true);
        weatherInteractor.getFreshCurrentWeatherForLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> successGetDataAction(data),
                        error -> errorGetDataAction(error));

    }

    private void successGetDataAction(CurrentWeatherDataModel data) {
        if (isViewAttached()) {
            getView().hideLoading();
            viewState.setLoading(false);
            viewState.setData(data);
            getView().showData(data);
        }
    }

    private void errorGetDataAction(Throwable error) {
        if (isViewAttached()) {
            getView().hideLoading();
            viewState.setLoading(false);
            getView().showError();
        }
    }

    private void prepareViewForRefreshData(@NonNull String location) {
        if (isViewAttached()) {
            getView().showLoading();
            viewState.setLoading(true);
            viewState.setLocation(location);
        }
    }


}
