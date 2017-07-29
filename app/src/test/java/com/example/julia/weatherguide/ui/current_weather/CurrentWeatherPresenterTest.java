package com.example.julia.weatherguide.ui.current_weather;

import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

import static com.example.julia.weatherguide.repositories.exception.ExceptionBundle.Reason.EMPTY_DATABASE;
import static com.example.julia.weatherguide.repositories.exception.ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED;
import static com.example.julia.weatherguide.repositories.exception.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CurrentWeatherPresenterTest {

    private static final String DATABASE_DESCRIPTION = "DATABASE";
    private static final double DATABASE_TEMPERATURE = 2567.2;
    private static final String DATABASE_LOCATION_NAME = "default city";

    private CurrentWeatherPresenter currentWeatherPresenter;
    private CurrentWeatherView currentWeatherView;
    private CurrentWeatherInteractor currentWeatherInteractor;

    @Before
    public void before() throws Exception {
        this.currentWeatherView = mock(CurrentWeatherView.class);
        this.currentWeatherInteractor = mock(CurrentWeatherInteractor.class);
        this.currentWeatherPresenter = new CurrentWeatherPresenter.Factory(currentWeatherInteractor)
            .create();
    }

    @Test
    public void onViewAttach_callsLoadCurrentWeather_showsWeather() {
        WeatherDataModel dummy = getDummyDatabaseWeather();
        when(currentWeatherInteractor.getCurrentWeather())
            .thenReturn(Single.just(dummy));

        currentWeatherPresenter.attachView(currentWeatherView);

        verify(currentWeatherView, times(1)).showLoading();
        verify(currentWeatherView, times(1)).hideLoading();
        verify(currentWeatherView, times(1)).showData(dummy);
        verify(currentWeatherView, never()).showCityNotPicked();
        verify(currentWeatherView, never()).showEmptyView();
        verify(currentWeatherView, never()).showNoInternet();
    }

    @Test
    public void onViewAttach_callsLoadCurrentWeather_locationNotInitialized() {
        when(currentWeatherInteractor.getCurrentWeather())
            .thenReturn(Single.error(new ExceptionBundle(LOCATION_NOT_INITIALIZED)));

        currentWeatherPresenter.attachView(currentWeatherView);

        verify(currentWeatherView, times(1)).showLoading();
        verify(currentWeatherView, times(1)).hideLoading();
        verify(currentWeatherView, times(1)).showCityNotPicked();
        verify(currentWeatherView, times(1)).showEmptyView();
        verify(currentWeatherView, never()).showNoInternet();
        verify(currentWeatherView, never()).showData(any(WeatherDataModel.class));
    }

    @Test
    public void onViewAttach_callsLoadCurrentWeather_networkUnavailable() {
        when(currentWeatherInteractor.getCurrentWeather())
            .thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));

        currentWeatherPresenter.attachView(currentWeatherView);

        verify(currentWeatherView, times(1)).showLoading();
        verify(currentWeatherView, times(1)).hideLoading();
        verify(currentWeatherView, times(1)).showNoInternet();
        verify(currentWeatherView, never()).showCityNotPicked();
        verify(currentWeatherView, never()).showEmptyView();
        verify(currentWeatherView, never()).showData(any(WeatherDataModel.class));
    }

    @Test
    public void onViewAttach_callsLoadCurrentWeather_emptyDatabase() {
        when(currentWeatherInteractor.getCurrentWeather())
            .thenReturn(Single.error(new ExceptionBundle(EMPTY_DATABASE)));

        currentWeatherPresenter.attachView(currentWeatherView);

        verify(currentWeatherView, times(1)).showLoading();
        verify(currentWeatherView, times(1)).hideLoading();
        verify(currentWeatherView, times(1)).showEmptyView();
        verify(currentWeatherView, never()).showNoInternet();
        verify(currentWeatherView, never()).showCityNotPicked();
        verify(currentWeatherView, never()).showData(any(WeatherDataModel.class));
    }

    @Test
    public void onViewAttach_onViewDetach_showsWeatherEachTimeDetaches() {
        WeatherDataModel dummy = getDummyDatabaseWeather();
        when(currentWeatherInteractor.getCurrentWeather())
            .thenReturn(Single.just(dummy));

        currentWeatherPresenter.attachView(currentWeatherView);
        currentWeatherPresenter.detachView();
        currentWeatherPresenter.attachView(currentWeatherView);
        currentWeatherPresenter.detachView();
        currentWeatherPresenter.attachView(currentWeatherView);

        verify(currentWeatherView, times(1)).showLoading();
        verify(currentWeatherView, times(1)).hideLoading();
        verify(currentWeatherView, times(3)).showData(dummy);
        verify(currentWeatherView, never()).showEmptyView();
        verify(currentWeatherView, never()).showNoInternet();
        verify(currentWeatherView, never()).showCityNotPicked();
    }

    @Test
    public void onViewAttach_onViewDetach_showLoadingAndWeatherAgain() {
        WeatherDataModel dummy = getDummyDatabaseWeather();
        when(currentWeatherInteractor.getCurrentWeather())
            .thenReturn(Single.just(dummy).delay(3, TimeUnit.SECONDS));

        currentWeatherPresenter.attachView(currentWeatherView);
        currentWeatherPresenter.detachView();
        currentWeatherPresenter.attachView(currentWeatherView);

        verify(currentWeatherView, timeout(4000).times(2)).showLoading();
        verify(currentWeatherView, timeout(4000).times(1)).hideLoading();
        verify(currentWeatherView, timeout(4000).times(1)).showData(dummy);
        verify(currentWeatherView, never()).showEmptyView();
        verify(currentWeatherView, never()).showNoInternet();
        verify(currentWeatherView, never()).showCityNotPicked();
    }

    @Test
    public void onViewAttach_onViewDetach_refreshCurrentWeather_networkUnavailable() {
        WeatherDataModel dummy = getDummyDatabaseWeather();
        when(currentWeatherInteractor.getCurrentWeather())
            .thenReturn(Single.just(dummy));
        when(currentWeatherInteractor.getFreshCurrentWeather())
            .thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));

        currentWeatherPresenter.attachView(currentWeatherView);
        currentWeatherPresenter.refreshCurrentWeather();

        verify(currentWeatherView, times(2)).showLoading();
        verify(currentWeatherView, times(2)).hideLoading();
        verify(currentWeatherView, times(1)).showData(dummy);
        verify(currentWeatherView, times(1)).showNoInternet();
        verify(currentWeatherView, never()).showEmptyView();
        verify(currentWeatherView, never()).showCityNotPicked();
    }

    @Test
    public void onViewAttach_onViewDetach_refreshCurrentWeather_success() {
        WeatherDataModel dummy = getDummyDatabaseWeather();
        when(currentWeatherInteractor.getCurrentWeather())
            .thenReturn(Single.just(dummy));
        when(currentWeatherInteractor.getFreshCurrentWeather())
            .thenReturn(Single.just(dummy));

        currentWeatherPresenter.attachView(currentWeatherView);
        currentWeatherPresenter.refreshCurrentWeather();

        verify(currentWeatherView, times(2)).showLoading();
        verify(currentWeatherView, times(2)).hideLoading();
        verify(currentWeatherView, times(2)).showData(dummy);
        verify(currentWeatherView, never()).showEmptyView();
        verify(currentWeatherView, never()).showNoInternet();
        verify(currentWeatherView, never()).showCityNotPicked();
    }



    private WeatherDataModel getDummyDatabaseWeather() {
        WeatherDataModel dummyWeather = new WeatherDataModel();
        dummyWeather.setLocationName(DATABASE_LOCATION_NAME);
        dummyWeather.setCurrentTemperature(String.valueOf(DATABASE_TEMPERATURE));
        dummyWeather.setWeatherDescription(DATABASE_DESCRIPTION);
        return dummyWeather;
    }
}
