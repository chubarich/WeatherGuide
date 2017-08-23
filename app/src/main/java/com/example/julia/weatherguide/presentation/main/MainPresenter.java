package com.example.julia.weatherguide.presentation.main;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.domain.use_cases.SubscribeOnCurrentLocationUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.menu.MenuPresenter;
import com.example.julia.weatherguide.utils.Optional;
import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.disposables.Disposable;


public class MainPresenter extends BasePresenter<MainView> {

    private final SubscribeOnCurrentLocationUseCase subscribeOnCurrentLocationUseCase;
    private Disposable subscribeOnCurrentLocationDisposable;

    private LocationWithId locationCache;

    public MainPresenter(SubscribeOnCurrentLocationUseCase useCase) {
        Preconditions.nonNull(useCase);
        subscribeOnCurrentLocationUseCase = useCase;
    }


    @Override
    protected void onViewAttached() {
        if (locationCache != null) {
            getView().onCurrentLocationChanged(locationCache);
            locationCache = null;
        }

        if (subscribeOnCurrentLocationDisposable == null) {
            subscribeOnCurrentLocationDisposable = subscribeOnCurrentLocationUseCase.execute(null)
                .subscribe(
                    this::showLocation,
                    error -> {}
                );
        }
    }

    @Override
    protected void onViewDetached() {

    }

    @Override
    protected void onDestroyed() {
        disposeSubscribeOnCurrentLocation();
    }

    // ----------------------------------------- private ------------------------------------------

    private void disposeSubscribeOnCurrentLocation() {
        if (subscribeOnCurrentLocationDisposable != null) {
            subscribeOnCurrentLocationDisposable.dispose();
        }
    }

    private void showLocation(Optional<LocationWithId> locationOptional) {
        if (getView() != null) {
            getView().onCurrentLocationChanged(locationOptional.get());
        } else {
            locationCache = locationOptional.get();
        }
    }

    // --------------------------------------- inner types ----------------------------------------

    public static class Factory implements PresenterFactory<MainPresenter, MainView> {

        private final SubscribeOnCurrentLocationUseCase subscribeOnCurrentLocationUseCase;

        public Factory(SubscribeOnCurrentLocationUseCase subscribeOnCurrentLocationUseCase) {
            this.subscribeOnCurrentLocationUseCase = subscribeOnCurrentLocationUseCase;
        }


        @Override
        public MainPresenter create() {
            return new MainPresenter(subscribeOnCurrentLocationUseCase);
        }
    }

}
