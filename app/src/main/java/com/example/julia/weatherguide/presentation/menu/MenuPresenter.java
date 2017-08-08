package com.example.julia.weatherguide.presentation.menu;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.domain.use_cases.AddLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.DeleteLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.GetLocationsAndSubscribeUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.VALUE_ALREADY_EXISTS;


public class MenuPresenter extends BasePresenter<MenuView> {

    private final GetLocationsAndSubscribeUseCase getLocations;
    private final DeleteLocationUseCase deleteLocationUseCase;
    private final AddLocationUseCase addLocationUseCase;
    private Disposable getLocationsDisposable;
    private final CompositeDisposable deleteLocationDisposable = new CompositeDisposable();
    private final CompositeDisposable addLocationDisposable = new CompositeDisposable();

    private MenuPresenter(GetLocationsAndSubscribeUseCase getLocations, DeleteLocationUseCase deleteLocationUseCase,
                          AddLocationUseCase addLocationUseCase) {
        Preconditions.nonNull(getLocations, deleteLocationUseCase, addLocationUseCase);
        this.getLocations = getLocations;
        this.deleteLocationUseCase = deleteLocationUseCase;
        this.addLocationUseCase = addLocationUseCase;
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

    public void onLocationRemoveClicked(final LocationWithTemperature location) {
        deleteLocationDisposable.add(
            deleteLocationUseCase.execute(location.location)
                .doOnSubscribe(d -> disposeGetLocations())
                .subscribe(() -> {
                        removeLocationFromAdapter(location);
                        runGetLocations();
                    }
                )
        );
    }

    public void onLocationAdded(final Location location) {
        addLocationDisposable.add(
            addLocationUseCase.execute(location)
                .doOnSubscribe(d -> disposeGetLocations())
                .subscribe(() -> {
                        addLocationToAdapter(location);
                        runGetLocations();
                    }
                )
        );
    }

    // ---------------------------------------- private -------------------------------------------

    private void disposeGetLocations() {
        if (getLocationsDisposable != null) {
            getLocationsDisposable.dispose();
        }
    }

    private void runGetLocations() {
        getLocationsDisposable = getLocations.execute(null)
            .doOnSubscribe(d -> disposeGetLocations())
            .subscribe(locations -> {
                    if (getView() != null) {
                        getView().setLocationsToAdapter(locations);
                    }
                }
            );
    }

    private void removeLocationFromAdapter(LocationWithTemperature location) {
        if (getView() != null) {
            getView().removeLocationFromAdapter(location);
        }
    }

    private void addLocationToAdapter(Location location) {
        if (getView() != null) {
            getView().addLocationToAdapter(new LocationWithTemperature(location, null));
        }
    }

    // -------------------------------------- inner types -----------------------------------------

    public static class Factory implements PresenterFactory<MenuPresenter, MenuView> {

        private final GetLocationsAndSubscribeUseCase getLocationsAndSubscribeUseCase;
        private final DeleteLocationUseCase deleteLocationUseCase;
        private final AddLocationUseCase addLocationUseCase;

        public Factory(GetLocationsAndSubscribeUseCase getLocationsAndSubscribeUseCase,
                       DeleteLocationUseCase deleteLocationUseCase,
                       AddLocationUseCase addLocationUseCase) {
            this.getLocationsAndSubscribeUseCase = getLocationsAndSubscribeUseCase;
            this.deleteLocationUseCase = deleteLocationUseCase;
            this.addLocationUseCase = addLocationUseCase;
        }

        @Override
        public MenuPresenter create() {
            return new MenuPresenter(getLocationsAndSubscribeUseCase, deleteLocationUseCase, addLocationUseCase);
        }
    }

}
