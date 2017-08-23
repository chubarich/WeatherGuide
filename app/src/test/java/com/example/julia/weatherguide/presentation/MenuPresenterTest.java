package com.example.julia.weatherguide.presentation;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.domain.use_cases.AddLocationAndSetAsCurrentUseCase;
import com.example.julia.weatherguide.domain.use_cases.DeleteLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.SubscribeOnLocationChangesUseCase;
import com.example.julia.weatherguide.domain.use_cases.base.ObservableUseCase;
import com.example.julia.weatherguide.presentation.menu.MenuPresenter;
import com.example.julia.weatherguide.presentation.menu.MenuView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.CURRENT_LOCATION_DELETION;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.NOT_DELETED;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;


public class MenuPresenterTest {

    private static final Location LOCATION = new Location(45.2, 22.1, "Moscow");
    private static final LocationWithTemperature LOCATION_WITH_TEMPERATURE
            = new LocationWithTemperature(LOCATION, 25);

    private SubscribeOnLocationChangesUseCase getLocations;
    private DeleteLocationUseCase deleteLocation;
    private AddLocationAndSetAsCurrentUseCase addLocation;
    private MenuPresenter menuPresenter;
    private MenuView menuView;

    @Before
    public void before() {
        menuView = mock(MenuView.class);

        getLocations = mock(SubscribeOnLocationChangesUseCase.class);
        deleteLocation = mock(DeleteLocationUseCase.class);
        addLocation = mock(AddLocationAndSetAsCurrentUseCase.class);
        menuPresenter = new MenuPresenter.Factory(getLocations, deleteLocation, addLocation).create();
    }

    @Test
    public void onAttach_setLocations() throws Exception {
        when(getLocations, "execute", any(Void.class))
                .thenReturn(Observable.just(new ArrayList<>()));

        menuPresenter.attachView(menuView);
        menuPresenter.detachView();
        menuPresenter.destroy();

        verify(getLocations, times(1)).execute(any(Void.class));
        verify(menuView, times(1)).setLocationsToAdapter(anyListOf(LocationWithTemperature.class));
    }

    @Test
    public void onLocationRemoveClicked_error() throws Exception {
        when(getLocations, "execute", any(Void.class))
                .thenReturn(Observable.just(new ArrayList<>()));
        when(deleteLocation, "execute", any(Location.class))
                .thenReturn(Completable.error(new ExceptionBundle(CURRENT_LOCATION_DELETION)));

        menuPresenter.attachView(menuView);
        menuPresenter.onLocationRemoveClicked(LOCATION_WITH_TEMPERATURE);
        menuPresenter.detachView();
        menuPresenter.destroy();

        verify(deleteLocation, times(1)).execute(any(Location.class));
        verify(menuView, times(1)).showCannotDeleteCurrentLocation();
    }

    @Test
    public void onLocationClicked() throws Exception {
        when(getLocations, "execute", any(Void.class))
                .thenReturn(Observable.just(new ArrayList<>()));
        when(addLocation, "execute", any(Void.class))
                .thenReturn(Completable.fromAction(()->{}));

        menuPresenter.attachView(menuView);
        menuPresenter.onLocationClicked(LOCATION_WITH_TEMPERATURE);
        menuPresenter.detachView();
        menuPresenter.destroy();

        verify(addLocation, times(1)).execute(any(Location.class));
    }

}
