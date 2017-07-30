package com.example.julia.weatherguide.ui.main;


import com.example.julia.weatherguide.interactors.MainViewInteractor;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    private MainPresenter mainPresenter;
    private MainViewInteractor mainViewInteractor;
    private MainView mainView;

    @Before
    public void before() throws Exception {
        mainViewInteractor = mock(MainViewInteractor.class);
        mainView = mock(MainView.class);
        mainPresenter = new MainPresenter.Factory(mainViewInteractor).create();
    }

    @Test
    public void onLocationChanged_showLocationChangedSuccess() throws Exception {
        when(mainViewInteractor.saveLocation(anyFloat(), anyFloat(), anyString()))
            .thenReturn(Completable.fromAction(() -> {}));

        mainPresenter.attachView(mainView);
        mainPresenter.onLocationChanged(5.5f, 3.3f, "Moscow");
        mainPresenter.onLocationChanged(2.0f, 355f, "Berlin");

        verify(mainView, never()).showLocationChangedException();
        verify(mainView, times(2)).showLocationChangedSuccess();
    }

    @Test
    public void onLocationChanged_showLocationChangedException() throws Exception {
        when(mainViewInteractor.saveLocation(anyFloat(), anyFloat(), anyString()))
            .thenReturn(Completable.error(new Exception("Any exception")));

        mainPresenter.attachView(mainView);
        mainPresenter.onLocationChanged(5.5f, 3.3f, "Moscow");
        mainPresenter.onLocationChanged(2.0f, 355f, "Berlin");

        verify(mainView, never()).showLocationChangedSuccess();
        verify(mainView, times(2)).showLocationChangedException();
    }

}
