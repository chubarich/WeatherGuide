package com.example.julia.weatherguide.presentation.settings;

import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.interactors.SettingsInteractorImpl;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SettingsPresenterTest {

    private SettingsInteractor settingsInteractor;
    private SettingsPresenter settingsPresenter;
    private SettingsView settingsView;

    @Before
    public void before() throws Exception {
        this.settingsView = mock(SettingsView.class);
        this.settingsInteractor = mock(SettingsInteractorImpl.class);
        this.settingsPresenter = new SettingsPresenter.Factory(settingsInteractor).create();
    }

    @Test
    public void onRefreshPeriodChanged_callsShowNumberFormatError() throws Exception {
        when(settingsInteractor.saveRefreshPeriod(anyInt()))
            .thenReturn(Completable.fromAction(()-> {}));

        settingsPresenter.attachView(settingsView);
        settingsPresenter.onRefreshPeriodChanged("124sfak");
        settingsPresenter.onRefreshPeriodChanged("nfn");
        settingsPresenter.onRefreshPeriodChanged("-2");
        settingsPresenter.onRefreshPeriodChanged("0");
        settingsPresenter.onRefreshPeriodChanged("0.5");
        settingsPresenter.onRefreshPeriodChanged("20");

        verify(settingsView, times(5)).showNumberFormatError();
    }

    @Test
    public void onRefreshPeriodChanged_callsLocationNotPickedError() throws Exception {
        when(settingsInteractor.saveRefreshPeriod(anyInt()))
            .thenReturn(Completable.error(new ExceptionBundle(LOCATION_NOT_INITIALIZED)));

        settingsPresenter.attachView(settingsView);
        settingsPresenter.onRefreshPeriodChanged("20");
        settingsPresenter.onRefreshPeriodChanged("50");

        verify(settingsView, never()).showNumberFormatError();
        verify(settingsView, never()).showPickTimeSuccess();
        verify(settingsView, times(2)).showLocationNotPickedError();
    }

    @Test
    public void onRefreshPeriodChanged_callsShowPickTimeSuccess() throws Exception {
        when(settingsInteractor.saveRefreshPeriod(anyInt()))
            .thenReturn(Completable.fromAction(() -> {}));

        settingsPresenter.attachView(settingsView);
        settingsPresenter.onRefreshPeriodChanged("20");
        settingsPresenter.onRefreshPeriodChanged("50");

        verify(settingsView, never()).showNumberFormatError();
        verify(settingsView, times(2)).showPickTimeSuccess();
        verify(settingsView, never()).showLocationNotPickedError();
    }
}
