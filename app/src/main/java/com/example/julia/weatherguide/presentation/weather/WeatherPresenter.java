package com.example.julia.weatherguide.presentation.weather;

import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import io.reactivex.disposables.CompositeDisposable;


public class WeatherPresenter extends BasePresenter<WeatherView> {

    private CompositeDisposable compositeDisposable;

    private WeatherPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    // --------------------------------------- BasePresenter ----------------------------------------

    @Override
    protected void onViewAttached() {
    }


    @Override
    protected void onViewDetached() {

    }

    @Override
    protected void onDestroyed() {
        compositeDisposable.dispose();
    }

    // ------------------------------------------- private ------------------------------------------

    private void onError(Throwable error) {
        if (error instanceof ExceptionBundle) {
            ExceptionBundle exceptionWithReason = (ExceptionBundle) error;
            if (exceptionWithReason.getReason() == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED) {
                // showCityNotPicked();
            } else if (exceptionWithReason.getReason() == ExceptionBundle.Reason.EMPTY_DATABASE) {
                // showEmptyView();
            } else if (exceptionWithReason.getReason() == ExceptionBundle.Reason.NETWORK_UNAVAILABLE) {
                // showNoInternet();
            }
        }
    }

    // --------------------------------------- inner types ----------------------------------------

    public static class Factory implements PresenterFactory<WeatherPresenter, WeatherView> {

        @Override
        public WeatherPresenter create() {
            return new WeatherPresenter();
        }
    }
}
