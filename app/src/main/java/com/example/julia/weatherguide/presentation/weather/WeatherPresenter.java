package com.example.julia.weatherguide.presentation.weather;

import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.managers.NetworkManager;
import com.example.julia.weatherguide.domain.use_cases.GetWeatherUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.disposables.Disposable;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.API_ERROR;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.EMPTY_DATABASE;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;
import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy.FROM_ANYWHERE;
import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy.FROM_CACHE;
import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy.FROM_NETWORK;
import static com.example.julia.weatherguide.domain.use_cases.GetWeatherUseCase.Args;
import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy;


public class WeatherPresenter extends BasePresenter<WeatherView> {

    private final NetworkManager networkManager;
    private final GetWeatherUseCase getWeatherUseCase;
    private Disposable disposable;

    private boolean firstStart = true;

    private WeatherPresenter(GetWeatherUseCase getWeatherUseCase, NetworkManager networkManager) {
        Preconditions.nonNull(getWeatherUseCase, networkManager);
        this.getWeatherUseCase = getWeatherUseCase;
        this.networkManager = networkManager;
    }

    // --------------------------------------- BasePresenter ----------------------------------------

    @Override
    protected void onViewAttached() {
        if (getView().isInitialized()) {
            GetWeatherStrategy strategy;
            if (firstStart) {
                strategy = FROM_ANYWHERE;
            } else {
                strategy = FROM_CACHE;
            }
            getWeather(strategy);
        }
    }


    @Override
    protected void onViewDetached() {
        if (disposable != null) disposable.dispose();
    }

    @Override
    protected void onDestroyed() {

    }

    // ----------------------------------------- public -------------------------------------------

    public void onRefresh() {
        getWeather(FROM_NETWORK);
    }

    // ----------------------------------------- private ------------------------------------------

    private void showWeather(Weather weather) {
        firstStart = false;
        if (getView() != null) {
            getView().hideLoading();
            getView().showWeather(weather);
        }
    }

    private void showException(Throwable throwable) {
        firstStart = false;
        if (getView() != null) {
            getView().hideLoading();

            if (throwable instanceof ExceptionBundle) {
                ExceptionBundle exception = (ExceptionBundle) throwable;
                if (exception.getReason() == NETWORK_UNAVAILABLE || exception.getReason() == EMPTY_DATABASE) {
                    getView().showNoInternet();
                } else if (exception.getReason() == API_ERROR) {
                    getView().showApiError();
                }
            }
        }
    }

    private void getWeather(GetWeatherStrategy strategy) {
        if (getView() != null && getView().isInitialized()) {
            if (disposable != null) disposable.dispose();

            if (isDisconnected()) {
                if (strategy == FROM_NETWORK) {
                    showException(new ExceptionBundle(NETWORK_UNAVAILABLE));
                    return;
                } else if (strategy == FROM_ANYWHERE) {
                    strategy = FROM_CACHE;
                }
            }

            final GetWeatherStrategy finalStrategy = strategy;
            disposable = getWeatherUseCase.execute(new Args(getView().getLocation(), strategy))
                .doOnSubscribe(d -> {
                    if (finalStrategy != FROM_CACHE) getView().showLoading();
                })
                .subscribe(this::showWeather, this::showException);
        }
    }

    private boolean isDisconnected() {
        return networkManager.getStatus() == NetworkManager.NetworkStatus.DISCONNECTED;
    }

    // --------------------------------------- inner types ----------------------------------------

    public static class Factory implements PresenterFactory<WeatherPresenter, WeatherView> {

        private final GetWeatherUseCase getWeatherUseCase;
        private final NetworkManager networkManager;

        public Factory(GetWeatherUseCase getWeatherUseCase, NetworkManager networkManager) {
            this.getWeatherUseCase = getWeatherUseCase;
            this.networkManager = networkManager;
        }

        @Override
        public WeatherPresenter create() {
            return new WeatherPresenter(getWeatherUseCase, networkManager);
        }
    }
}
