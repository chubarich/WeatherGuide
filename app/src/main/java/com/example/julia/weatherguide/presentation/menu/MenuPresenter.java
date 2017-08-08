package com.example.julia.weatherguide.presentation.menu;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;
import com.example.julia.weatherguide.domain.use_cases.AddLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.DeleteLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.SubscribeOnLocationChangesUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class MenuPresenter extends BasePresenter<MenuView> {

    private final SubscribeOnLocationChangesUseCase getLocations;
    private final DeleteLocationUseCase deleteLocationUseCase;
    private final AddLocationUseCase addLocationUseCase;
    private Disposable getLocationsDisposable;
    private final CompositeDisposable deleteLocationDisposable = new CompositeDisposable();
    private final CompositeDisposable addLocationDisposable = new CompositeDisposable();

    private MenuPresenter(SubscribeOnLocationChangesUseCase getLocations,
                          DeleteLocationUseCase deleteLocationUseCase,
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
                .subscribe(()->{}, error -> {})
        );
    }

    public void onLocationAdded(final Location location) {
        addLocationDisposable.add(
            addLocationUseCase.execute(location)
                .subscribe(()->{}, error -> {})
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

    // -------------------------------------- inner types -----------------------------------------

    public static class Factory implements PresenterFactory<MenuPresenter, MenuView> {

        private final SubscribeOnLocationChangesUseCase subscribeOnLocationChangesUseCase;
        private final DeleteLocationUseCase deleteLocationUseCase;
        private final AddLocationUseCase addLocationUseCase;

        public Factory(SubscribeOnLocationChangesUseCase subscribeOnLocationChangesUseCase,
                       DeleteLocationUseCase deleteLocationUseCase,
                       AddLocationUseCase addLocationUseCase) {
            this.subscribeOnLocationChangesUseCase = subscribeOnLocationChangesUseCase;
            this.deleteLocationUseCase = deleteLocationUseCase;
            this.addLocationUseCase = addLocationUseCase;
        }

        @Override
        public MenuPresenter create() {
            return new MenuPresenter(subscribeOnLocationChangesUseCase, deleteLocationUseCase, addLocationUseCase);
        }
    }

}
