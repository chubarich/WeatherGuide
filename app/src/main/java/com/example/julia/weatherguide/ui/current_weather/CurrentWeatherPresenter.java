package com.example.julia.weatherguide.ui.current_weather;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by julia on 16.07.17.
 */

public class CurrentWeatherPresenter extends BasePresenter<CurrentWeatherView> {

    @Inject
    CurrentWeatherInteractor weatherInteractor;

    private static CurrentWeatherViewState viewState = new CurrentWeatherViewState();

    public CurrentWeatherPresenter(CurrentWeatherInteractor interactor) {
        //TODO: is this right?
        WeatherGuideApplication.getInstance().plusCurrentWeatherComponent().inject(this);

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
        if (isViewAttached()) {
            getView().showLoading();
            viewState.setLoading(true);
            viewState.setLocation(location);
        }
        weatherInteractor.getCurrentWeatherForLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                viewState.setLoading(false);
                                viewState.setData(data);
                                getView().showData(data);
                            }
                        },
                        error -> {
                            if (isViewAttached()) {
                                if (viewState.hasData()) {
                                    getView().hideLoading();
                                    viewState.setLoading(false);
                                    getView().showError();
                                } else {
                                    getView().hideLoading();
                                    viewState.setLoading(false);
                                    getView().showEmptyView();
                                }
                            }
                        });

    }


    public void refreshCurrentWeather(@NonNull String location) {
        if (isViewAttached()) {
            getView().showLoading();
            viewState.setLoading(true);
            viewState.setLocation(location);
            viewState.setReasonForLoadingUpdate(true);
        }
        weatherInteractor.getFreshCurrentWeatherForLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                viewState.setLoading(false);
                                viewState.setData(data);
                                getView().showData(data);
                            }
                        },
                        error -> {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                viewState.setLoading(false);
                                getView().showError();
                            }
                        });

    }

}
