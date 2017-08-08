package com.example.julia.weatherguide.presentation.choose_location;


import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.domain.use_cases.GetLocationFromPredictionUseCase;
import com.example.julia.weatherguide.domain.use_cases.GetLocationPredictionsUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class ChooseLocationPresenter extends BasePresenter<ChooseLocationView> {

    private final GetLocationPredictionsUseCase getLocationPredictionsUseCase;
    private final GetLocationFromPredictionUseCase getLocationFromPredictionUseCase;
    private Disposable getLocationPredictionsDisposable;
    private Disposable getLocationDisposable;

    public ChooseLocationPresenter(GetLocationPredictionsUseCase getLocationPredictionsUseCase,
                                   GetLocationFromPredictionUseCase getLocationFromPredictionUseCase) {
        Preconditions.nonNull(getLocationPredictionsUseCase, getLocationFromPredictionUseCase);
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
        disposePredictions();
    }

    // --------------------------------------- public ---------------------------------------------

    public void onInputPhraseChanged(String phrase) {
        getLocationPredictionsDisposable = getPredictions(phrase)
            .doOnSubscribe(d -> disposePredictions())
            .doAfterTerminate(this::hideProgressBar)
            .subscribe(predictions -> showResults(predictions, phrase),
                error -> showError(error, true));
    }

    public void onLocationPredictionChosen(LocationPrediction locationPrediction) {
        getLocationDisposable = getLocation(locationPrediction)
            .doOnSubscribe(d -> disposeGetLocationDisposable())
            .subscribe(this::finishLocationChoosing,
                error -> showError(error, false)
            );
    }

    // --------------------------------------- private --------------------------------------------

    private void disposePredictions() {
        if (getLocationPredictionsDisposable != null) {
            getLocationPredictionsDisposable.dispose();
        }
        if (getLocationDisposable != null) {
            getLocationDisposable.dispose();
        }
    }

    private void disposeGetLocationDisposable() {
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

    private void hideProgressBar() {
        if (getView() != null) {
            getView().hideProgressBar();
        }
    }

    private void showResults(List<LocationPrediction> predictions, String request) {
        if (getView() != null) {
            getView().hideProgressBar();
            getView().showResults(predictions, request);
        }
    }

    private void finishLocationChoosing(Location location) {
        if (getView() != null) {
            getView().finishLocationChoosing(location);
        }
    }

    private void showError(Throwable error, boolean showAsOverlay) {
        if (getView() != null) {
            getView().hideProgressBar();
            if (error instanceof ExceptionBundle) {
                if (((ExceptionBundle) error).getReason() == ExceptionBundle.Reason.NETWORK_UNAVAILABLE) {
                    getView().showNoInternet(showAsOverlay);
                }
            }
        }
    }

    // ------------------------------------- inner types ------------------------------------------

    public static class Factory implements PresenterFactory<ChooseLocationPresenter, ChooseLocationView> {

        private final GetLocationPredictionsUseCase getLocationPredictionsUseCase;
        private final GetLocationFromPredictionUseCase getLocationFromPredictionUseCase;

        public Factory(GetLocationPredictionsUseCase getLocationPredictionsUseCase,
                       GetLocationFromPredictionUseCase getLocationFromPredictionUseCase) {
            this.getLocationPredictionsUseCase = getLocationPredictionsUseCase;
            this.getLocationFromPredictionUseCase = getLocationFromPredictionUseCase;
        }

        @Override
        public ChooseLocationPresenter create() {
            return new ChooseLocationPresenter(getLocationPredictionsUseCase, getLocationFromPredictionUseCase);
        }

    }
}
