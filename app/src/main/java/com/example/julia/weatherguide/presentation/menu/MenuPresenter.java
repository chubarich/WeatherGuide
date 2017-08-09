package com.example.julia.weatherguide.presentation.menu;

import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.domain.use_cases.AddLocationAndSetAsCurrentUseCase;
import com.example.julia.weatherguide.domain.use_cases.DeleteLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.SubscribeOnLocationChangesUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.CURRENT_LOCATION_DELETION;


public class MenuPresenter extends BasePresenter<MenuView> {

    private final SubscribeOnLocationChangesUseCase getLocations;
    private final DeleteLocationUseCase deleteLocationUseCase;
    private final AddLocationAndSetAsCurrentUseCase addLocationAndSetAsCurrentUseCase;
    private Disposable getLocationsDisposable;
    private final CompositeDisposable deleteLocationDisposable = new CompositeDisposable();
    private final CompositeDisposable addLocationDisposable = new CompositeDisposable();

    private MenuPresenter(SubscribeOnLocationChangesUseCase getLocations,
                          DeleteLocationUseCase deleteLocationUseCase,
                          AddLocationAndSetAsCurrentUseCase addLocationAndSetAsCurrentUseCase) {
        Preconditions.nonNull(getLocations, deleteLocationUseCase, addLocationAndSetAsCurrentUseCase);
        this.getLocations = getLocations;
        this.deleteLocationUseCase = deleteLocationUseCase;
        this.addLocationAndSetAsCurrentUseCase = addLocationAndSetAsCurrentUseCase;
    }

    // --------------------------------------- lifecycle ------------------------------------------

    @Override
    protected void onViewAttached() {
        runGetLocations();
    }

    @Override
    protected void onViewDetached() {
        disposeGetLocations();
    }

    @Override
    protected void onDestroyed() {
        deleteLocationDisposable.dispose();
        addLocationDisposable.dispose();
    }

    // ---------------------------------------- public --------------------------------------------

    public void onLocationRemoveClicked(LocationWithTemperature location) {
        deleteLocationDisposable.add(
            deleteLocationUseCase.execute(location.location)
                .subscribe(
                    () -> {},
                    this::handleRemoveLocationError
                )
        );
    }

    public void onLocationClicked(LocationWithTemperature location) {
        addLocationDisposable.add(
            addLocationAndSetAsCurrentUseCase.execute(location.location)
                .subscribe(
                    () -> {},
                    error -> {}
                )
        );
    }

    // ---------------------------------------- private -------------------------------------------

    private void runGetLocations() {
        getLocationsDisposable = getLocations.execute(null)
            .subscribe(locations -> {
                    if (getView() != null) {
                        getView().setLocationsToAdapter(locations);
                    }
                }
            );
    }

    private void disposeGetLocations() {
        if (getLocationsDisposable != null) {
            getLocationsDisposable.dispose();
        }
    }

    private void handleRemoveLocationError(Throwable error) {
        if (error instanceof ExceptionBundle) {
            ExceptionBundle exceptionWithReason = (ExceptionBundle) error;
            if (exceptionWithReason.getReason() == CURRENT_LOCATION_DELETION) {
                if (getView() != null) {
                    getView().showCannotDeleteCurrentLocation();
                }
            }
        }
    }

    // -------------------------------------- inner types -----------------------------------------

    public static class Factory implements PresenterFactory<MenuPresenter, MenuView> {

        private final SubscribeOnLocationChangesUseCase subscribeOnLocationChangesUseCase;
        private final DeleteLocationUseCase deleteLocationUseCase;
        private final AddLocationAndSetAsCurrentUseCase addLocationAndSetAsCurrentUseCase;

        public Factory(SubscribeOnLocationChangesUseCase subscribeOnLocationChangesUseCase,
                       DeleteLocationUseCase deleteLocationUseCase,
                       AddLocationAndSetAsCurrentUseCase addLocationAndSetAsCurrentUseCase) {
            this.subscribeOnLocationChangesUseCase = subscribeOnLocationChangesUseCase;
            this.deleteLocationUseCase = deleteLocationUseCase;
            this.addLocationAndSetAsCurrentUseCase = addLocationAndSetAsCurrentUseCase;
        }

        @Override
        public MenuPresenter create() {
            return new MenuPresenter(subscribeOnLocationChangesUseCase, deleteLocationUseCase, addLocationAndSetAsCurrentUseCase);
        }
    }

}
