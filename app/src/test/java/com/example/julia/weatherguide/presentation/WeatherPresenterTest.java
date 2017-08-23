package com.example.julia.weatherguide.presentation;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.weather.CurrentWeather;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.managers.NetworkManager;
import com.example.julia.weatherguide.domain.use_cases.GetWeatherUseCase;
import com.example.julia.weatherguide.presentation.weather.WeatherPresenter;
import com.example.julia.weatherguide.presentation.weather.WeatherView;

import com.example.julia.weatherguide.domain.use_cases.GetWeatherUseCase.Args;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

public class WeatherPresenterTest {

    private WeatherPresenter weatherPresenter;
    private WeatherView weatherView;

    private GetWeatherUseCase getWeather;
    private NetworkManager networkManager;

    @Before
    public void before() {
        weatherView = mock(WeatherView.class);
        getWeather = mock(GetWeatherUseCase.class);
        networkManager = mock(NetworkManager.class);
        weatherPresenter = new WeatherPresenter.Factory(getWeather, networkManager).create();
    }

    @Test
    public void onAttach_getsWeather() throws Exception{
        when(getWeather, "execute", any()).thenReturn(Single.never());
        when(networkManager, "getStatus").thenReturn(NetworkManager.NetworkStatus.CONNECTED);
        when(weatherView, "isInitialized").thenReturn(true);
        when(weatherView, "getLocation").thenReturn(new LocationWithId(1, new Location(5, 3, "Moscow")));

        weatherPresenter.attachView(weatherView);
        weatherPresenter.detachView();
        weatherPresenter.attachView(weatherView);
        weatherPresenter.detachView();
        weatherPresenter.destroy();

        verify(getWeather, times(2)).execute(any(Args.class));
        verify(weatherView, times(2)).showLoading();
    }

    @Test
    public void refresh_getsWeather() throws Exception{
        when(getWeather, "execute", any(Args.class)).thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));
        when(networkManager, "getStatus").thenReturn(NetworkManager.NetworkStatus.DISCONNECTED);
        when(weatherView, "isInitialized").thenReturn(true);
        when(weatherView, "getLocation").thenReturn(new LocationWithId(1, new Location(5, 3, "Moscow")));

        weatherPresenter.attachView(weatherView);
        weatherPresenter.onRefresh();
        weatherPresenter.detachView();
        weatherPresenter.destroy();

        verify(getWeather, times(1)).execute(any(Args.class));
        verify(weatherView, times(0)).showLoading();
        verify(weatherView, times(2)).showNoInternet();
    }

}
