package com.example.julia.weatherguide.ui.current_weather;

import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;
import com.example.julia.weatherguide.ui.base.presenter.BasePresenter;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;

import io.reactivex.disposables.CompositeDisposable;

public class CurrentWeatherPresenter extends BasePresenter<CurrentWeatherView> {

    private CurrentWeatherViewState viewState = new CurrentWeatherViewState();

    private final CurrentWeatherInteractor currentWeatherInteractor;
    private CompositeDisposable compositeDisposable;

    private CurrentWeatherPresenter(CurrentWeatherInteractor currentWeatherInteractor) {
        this.currentWeatherInteractor = currentWeatherInteractor;
        compositeDisposable = new CompositeDisposable();
    }

    // --------------------------------------- BasePresenter ----------------------------------------

    @Override
    protected void onViewAttached() {
        if (viewState.isLoading) {
            prepareViewForRefreshData();
        }

        if (viewState.hasData()) {
            getView().showData(viewState.getData());
        }

        if (viewState.firstStart) {
            viewState.setFirstStart(false);
            loadCurrentWeather();
        }
    }


    @Override
    protected void onViewDetached() {

    }

    @Override
    protected void onDestroyed() {
        compositeDisposable.dispose();
    }

    // ---------------------------------------- public ----------------------------------------------

    public void refreshCurrentWeather() {
        prepareViewForRefreshData();
        compositeDisposable.add(
            currentWeatherInteractor.getFreshCurrentWeather()
                .subscribe(
                    this::successWeatherData,
                    this::onError
                )
        );
    }

    // ----------------------------------------- private --------------------------------------------

    private void loadCurrentWeather() {
        prepareViewForRefreshData();
        compositeDisposable.add(
            currentWeatherInteractor.getCurrentWeather()
                .subscribe(
                    this::successWeatherData,
                    this::onError
                )
        );
    }

    // ------------------------------------------- private ------------------------------------------

    private void onError(Throwable error) {
        if (error instanceof ExceptionBundle) {
            ExceptionBundle exceptionWithReason = (ExceptionBundle) error;
            // TODO: change interactors to throw only ExceptionBundle for proper exception handling
            if (exceptionWithReason.getReason() == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED) {
                showCityNotPicked();
            } else if (exceptionWithReason.getReason() == ExceptionBundle.Reason.EMPTY_DATABASE) {
                showEmptyView();
            }
        } else {
            if (viewState.hasData()) {
                showNoInternet();
            } else {
                showEmptyView();
            }
        }
    }

    private void successWeatherData(WeatherDataModel data) {
        viewState.setLoading(false);
        viewState.setData(data);
        if (isViewAttached()) {
            getView().hideLoading();
            getView().showData(data);
        }
    }

    private void showEmptyView() {
        viewState.setLoading(false);
        if (isViewAttached()) {
            getView().hideLoading();
            getView().showEmptyView();
        }
    }

    private void showNoInternet() {
        viewState.setLoading(false);
        if (isViewAttached()) {
            getView().hideLoading();
            getView().showNoInternet();
        }
    }

    private void showCityNotPicked() {
        viewState.setLoading(false);
        if (isViewAttached()) {
            getView().hideLoading();
            getView().showEmptyView();
            getView().showCityNotPicked();
        }
    }

    private void prepareViewForRefreshData() {
        viewState.setLoading(true);
        if (isViewAttached()) {
            getView().showLoading();
        }
    }

// ---------------------------------------- inner types ----------------------------------------

    class CurrentWeatherViewState {

        private boolean isLoading;
        private WeatherDataModel data;
        private boolean firstStart = true;

        private void setLoading(boolean loading) {
            isLoading = loading;
        }

        public void setData(WeatherDataModel data) {
            this.data = data;
        }

        private void setFirstStart(boolean firstStart) {
            this.firstStart = firstStart;
        }

        public WeatherDataModel getData() {
            return data;
        }

        private boolean hasData() {
            return data != null;
        }
    }

    public static class Factory implements PresenterFactory<CurrentWeatherPresenter, CurrentWeatherView> {

        private final CurrentWeatherInteractor currentWeatherInteractor;

        public Factory(CurrentWeatherInteractor currentWeatherInteractor) {
            this.currentWeatherInteractor = currentWeatherInteractor;
        }

        @Override
        public CurrentWeatherPresenter create() {
            return new CurrentWeatherPresenter(currentWeatherInteractor);
        }
    }
}
