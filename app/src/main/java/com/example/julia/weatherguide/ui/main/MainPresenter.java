package com.example.julia.weatherguide.ui.main;

import com.example.julia.weatherguide.interactors.MainViewInteractor;
import com.example.julia.weatherguide.ui.base.presenter.BasePresenter;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;

import io.reactivex.disposables.CompositeDisposable;

public class MainPresenter extends BasePresenter<MainView> {

    private final MainViewInteractor mainViewInteractor;
    private final CompositeDisposable compositeDisposable;

    private MainPresenter(MainViewInteractor mainViewInteractor) {
        this.mainViewInteractor = mainViewInteractor;
        this.compositeDisposable = new CompositeDisposable();
    }

    // ---------------------------------------- lifecycle -------------------------------------------

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

    // ----------------------------------------- public ---------------------------------------------

    public void onLocationChanged(float longitude, float latitude, String cityName) {
        compositeDisposable.add(
            mainViewInteractor.saveLocation(longitude, latitude, cityName)
                .subscribe(
                    this::showLocationChangedSuccess,
                    throwable -> showLocationChangedException()
                )
        );
    }

    // ---------------------------------------- private ---------------------------------------------

    private void showLocationChangedSuccess() {
        if (isViewAttached()) {
            getView().showLocationChangedSuccess();
        }
    }

    private void showLocationChangedException() {
        if (isViewAttached()) {
            getView().showLocationChangedException();
        }
    }

    // ---------------------------------------- Factory ---------------------------------------------

    public static class Factory implements PresenterFactory<MainPresenter, MainView> {

        private final MainViewInteractor mainViewInteractor;

        public Factory(final MainViewInteractor mainViewInteractor) {
            this.mainViewInteractor = mainViewInteractor;
        }

        @Override
        public MainPresenter create() {
            return new MainPresenter(mainViewInteractor);
        }
    }

}
