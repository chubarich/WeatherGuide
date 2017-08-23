package com.example.julia.weatherguide.presentation.choose_location;


import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.managers.NetworkManager;
import com.example.julia.weatherguide.domain.use_cases.GetLocationFromPredictionUseCase;
import com.example.julia.weatherguide.domain.use_cases.GetLocationPredictionsUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

import static com.example.julia.weatherguide.data.managers.NetworkManager.NetworkStatus.DISCONNECTED;

public class ChooseLocationPresenter extends BasePresenter<ChooseLocationView> {

    private final NetworkManager networkManager;
    private final GetLocationPredictionsUseCase getLocationPredictionsUseCase;
    private final GetLocationFromPredictionUseCase getLocationFromPredictionUseCase;
    private Disposable getLocationPredictionsDisposable;
    private Disposable getLocationDisposable;

    public ChooseLocationPresenter(NetworkManager networkManager,
                                   GetLocationPredictionsUseCase getLocationPredictionsUseCase,
                                   GetLocationFromPredictionUseCase getLocationFromPredictionUseCase) {
        Preconditions.nonNull(networkManager, getLocationPredictionsUseCase, getLocationFromPredictionUseCase);
        this.networkManager = networkManager;
        this.getLocationPredictionsUseCase = getLocationPredictionsUseCase;
        this.getLocationFromPredictionUseCase = getLocationFromPredictionUseCase;
    }

    // ------------------------------------ BasePresenter -----------------------------------------

    @Override
    protected void onViewAttached() {

    }

    @Override
    protected void onViewDetached() {
    }

    @Override
    protected void onDestroyed() {
        disposeAllDisposables();
    }

    // --------------------------------------- public ---------------------------------------------

    public void onInputPhraseChanged(String phrase) {
        if (networkManager.getStatus() == DISCONNECTED) {
            showNoInternet(true);
        } else {
            getLocationPredictionsDisposable = getPredictions(phrase)
                .doOnSubscribe(d -> disposeAllDisposables())
                .subscribe(predictions -> showResults(predictions, phrase),
                    error -> showError(error, true));
        }
    }

    public void onLocationPredictionChosen(LocationPrediction locationPrediction) {
        if (networkManager.getStatus() == DISCONNECTED) {
            showNoInternet(false);
        } else {
            getLocationDisposable = getLocation(locationPrediction)
                .subscribe(location -> finish(),
                    error -> showError(error, false)
                );
        }
    }

    public void disposePredictions() {
        if (getLocationPredictionsDisposable != null) {
            getLocationPredictionsDisposable.dispose();
        }
    }

    // --------------------------------------- private --------------------------------------------

    private void disposeAllDisposables() {
        disposePredictions();
        disposeGetLocation();
    }

    private void disposeGetLocation() {
        if (getLocationDisposable != null) {
            getLocationDisposable.dispose();
        }
    }

    private Single<List<LocationPrediction>> getPredictions(String phrase) {
        if (phrase.isEmpty()) {
            return Single.just(new ArrayList<>());
        } else {
            return getLocationPredictionsUseCase.execute(phrase);
        }
    }

    private Single<Location> getLocation(LocationPrediction prediction) {
        return getLocationFromPredictionUseCase.execute(prediction);
    }

    private void showResults(List<LocationPrediction> predictions, String request) {
        if (getView() != null) {
            getView().hideProgressBar();
            getView().showResults(predictions, request);
        }
    }

    private void finish() {
        if (getView() != null) {
            getView().finishChoosing();
        }
    }

    private void showError(Throwable error, boolean showAsOverlay) {
        if (error instanceof ExceptionBundle) {
            if (((ExceptionBundle) error).getReason() == ExceptionBundle.Reason.NETWORK_UNAVAILABLE) {
                showNoInternet(showAsOverlay);
            }
        }
    }

    private void showNoInternet(boolean showAsOverlay) {
        getView().hideProgressBar();
        if (getView() != null) {
            getView().showNoInternet(showAsOverlay);
        }
    }

    // ------------------------------------- inner types ------------------------------------------

    public static class Factory implements PresenterFactory<ChooseLocationPresenter, ChooseLocationView> {

        private final NetworkManager networkManager;
        private final GetLocationPredictionsUseCase getLocationPredictionsUseCase;
        private final GetLocationFromPredictionUseCase getLocationFromPredictionUseCase;

        public Factory(NetworkManager networkManager,
                       GetLocationPredictionsUseCase getLocationPredictionsUseCase,
                       GetLocationFromPredictionUseCase getLocationFromPredictionUseCase) {
            this.networkManager = networkManager;
            this.getLocationPredictionsUseCase = getLocationPredictionsUseCase;
            this.getLocationFromPredictionUseCase = getLocationFromPredictionUseCase;
        }

        @Override
        public ChooseLocationPresenter create() {
            return new ChooseLocationPresenter(networkManager, getLocationPredictionsUseCase,
                getLocationFromPredictionUseCase);
        }

    }
}
