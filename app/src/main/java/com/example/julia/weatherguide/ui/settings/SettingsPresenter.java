package com.example.julia.weatherguide.ui.settings;

import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.ui.base.presenter.BasePresenter;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;

import io.reactivex.disposables.CompositeDisposable;

public class SettingsPresenter extends BasePresenter<SettingsView> {

    private final SettingsInteractor interactor;
    private final CompositeDisposable compositeDisposable;

    private SettingsPresenter(SettingsInteractor interactor) {
        this.interactor = interactor;
        this.compositeDisposable = new CompositeDisposable();
    }

    // ------------------------------------ BasePresenter -------------------------------------------

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

    // ------------------------------------------- public -------------------------------------------

    void onRefreshPeriodChanged(String newValue) {
        try {
            int period = Integer.valueOf(newValue);
            if (period > 0) {
                compositeDisposable.add(
                    interactor.saveRefreshPeriod(period)
                        .subscribe(
                            () -> {
                                if (isViewAttached()) getView().showPickTimeSuccess();
                            },
                            error -> {
                                if (isViewAttached()) getView().showLocationNotPickedError();
                            }
                        )
                );
            } else {
                getView().showNumberFormatError();
            }
        } catch (NumberFormatException e) {
            getView().showNumberFormatError();
        }
    }

    // ------------------------------------------ Factory -------------------------------------------

    public static class Factory implements PresenterFactory<SettingsPresenter, SettingsView> {

        private final SettingsInteractor settingsInteractor;

        public Factory(SettingsInteractor settingsInteractor) {
            this.settingsInteractor = settingsInteractor;
        }

        @Override
        public SettingsPresenter create() {
            return new SettingsPresenter(settingsInteractor);
        }
    }
}
