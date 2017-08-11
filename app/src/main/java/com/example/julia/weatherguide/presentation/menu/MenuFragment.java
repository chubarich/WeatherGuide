package com.example.julia.weatherguide.presentation.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;
import com.example.julia.weatherguide.presentation.about.AboutActivity;
import com.example.julia.weatherguide.presentation.application.WeatherGuideApplication;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.view.BaseFragment;
import com.example.julia.weatherguide.presentation.choose_location.ChooseLocationActivity;
import com.example.julia.weatherguide.presentation.main.DrawerView;
import com.example.julia.weatherguide.presentation.settings.SettingsActivity;
import com.example.julia.weatherguide.utils.ChooseLocationContract;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.disposables.CompositeDisposable;


public class MenuFragment extends BaseFragment<MenuPresenter, MenuView>
    implements MenuView {

    private ScrollView scrollView;
    private RecyclerView recyclerView;
    private LinearLayout layoutMenu;
    private LinearLayout layoutBottomSubmenu;
    private LinearLayout layoutTopSubmenu;
    private View buttonAbout;
    private View buttonSettings;
    private View buttonEditLocations;
    private View buttonAddLocations;

    private final CompositeDisposable rxBindingDisposable = new CompositeDisposable();

    @Inject
    Provider<PresenterFactory<MenuPresenter, MenuView>> presenterProvider;

    // ------------------------------------ new instance ------------------------------------------

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    // -------------------------------------- lifecycle -------------------------------------------

    @Override
    public void onAttach(Context context) {
        if (!(context instanceof DrawerView)) {
            throw new IllegalStateException("Paren activity must be instance of DrawerView");
        }

        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ((WeatherGuideApplication) getActivity().getApplication())
            .getMenuComponent()
            .inject(this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view);
    }

    @Override
    public void onDestroyView() {
        rxBindingDisposable.clear();
        if (getLocationModel() != null) getLocationModel().detachCallbacks();
        super.onDestroyView();
    }

    // --------------------------------------- MenuView -------------------------------------------

    @Override
    public void setLocationsToAdapter(List<LocationWithTemperature> locations) {
        getLocationModel().attachCallbacks(getMenuModelCallbacks());
        getLocationModel().setupLocations(locations);

        layoutMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDrawerClosed() {
        setLocationModelDeletionModeFalse();
    }

    @Override
    public void showCannotDeleteCurrentLocation() {
        Toast.makeText(getContext(), getString(R.string.current_location_deletion), Toast.LENGTH_SHORT).show();
    }

    // ------------------------------------- BaseFragment -----------------------------------------

    @Override
    protected MenuView getViewInterface() {
        return this;
    }

    @Override
    protected PresenterFactory<MenuPresenter, MenuView> getPresenterFactory() {
        return presenterProvider.get();
    }

    @Override
    protected int getFragmentId() {
        return this.getClass().getSimpleName().hashCode();
    }

    @Override
    @LayoutRes
    protected int getLayoutRes() {
        return R.layout.fragment_menu;
    }

    // ---------------------------------------- private -------------------------------------------

    private void initializeView(View view) {
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        layoutMenu = (LinearLayout) view.findViewById(R.id.layout_menu);
        layoutMenu.setVisibility(View.INVISIBLE);
        layoutBottomSubmenu = (LinearLayout) view.findViewById(R.id.layout_bottom_submenu);
        layoutTopSubmenu = (LinearLayout) view.findViewById(R.id.layout_top_submenu);

        LocationAdapter locationAdapter = new LocationAdapter(
            ContextCompat.getColor(getContext(), android.R.color.black),
            ContextCompat.getColor(getContext(), android.R.color.transparent)
        );
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(locationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        buttonAbout = view.findViewById(R.id.button_about);
        buttonSettings = view.findViewById(R.id.button_settings);
        buttonAddLocations = view.findViewById(R.id.button_add_locations);
        buttonEditLocations = view.findViewById(R.id.button_edit_locations);
        buttonEditLocations.setVisibility(View.GONE);

        setListeners();
    }

    private void setListeners() {
        View.OnClickListener deletionModeListener = v -> setLocationModelDeletionModeFalse();
        scrollView.setOnClickListener(deletionModeListener);
        layoutMenu.setOnClickListener(deletionModeListener);
        layoutBottomSubmenu.setOnClickListener(deletionModeListener);
        layoutTopSubmenu.setOnClickListener(deletionModeListener);
        recyclerView.setOnClickListener(deletionModeListener);

        rxBindingDisposable.add(
            RxView.clicks(buttonAddLocations).subscribe(o -> {
                if (canClickMenuButtons()) startActivityForResult(
                    new Intent(getContext(), ChooseLocationActivity.class),
                    ChooseLocationContract.REQUEST_CODE
                );
            })
        );

        rxBindingDisposable.add(
            RxView.clicks(buttonAbout).subscribe(o -> {
                if (canClickMenuButtons()) {
                    ((DrawerView)getActivity()).closeDrawer();
                    startActivity(new Intent(getContext(), AboutActivity.class));
                }
            })
        );

        rxBindingDisposable.add(
            RxView.clicks(buttonSettings).subscribe(o -> {
                if (canClickMenuButtons()) {
                    ((DrawerView)getActivity()).closeDrawer();
                    startActivity(new Intent(getContext(), SettingsActivity.class));
                }
            })
        );

        rxBindingDisposable.add(
            RxView.clicks(buttonEditLocations).subscribe(o -> {
                getLocationModel().setDeletionMode(!getLocationModel().isInDeletionMode());
            })
        );
    }

    private LocationModel getLocationModel() {
        return (LocationModel) recyclerView.getAdapter();
    }

    private boolean canClickMenuButtons() {
        if (getLocationModel() == null) return false;

        if (getLocationModel().isInDeletionMode()) {
            getLocationModel().setDeletionMode(false);
            return false;
        }

        return true;
    }

    private void setLocationModelDeletionModeFalse() {
        if (getLocationModel() != null) {
            getLocationModel().setDeletionMode(false);
        }
    }

    private LocationModel.Callbacks getMenuModelCallbacks() {
        return new LocationModel.Callbacks() {
            @Override
            public void onLocationClicked(LocationWithTemperature location) {
                if (!getLocationModel().isInDeletionMode()) {
                    ((DrawerView)getActivity()).closeDrawer();
                    ((MenuPresenter) getPresenter()).onLocationClicked(location);
                }
            }

            @Override
            public void onLocationRemoveClicked(LocationWithTemperature location) {
                ((MenuPresenter) getPresenter()).onLocationRemoveClicked(location);
            }

            @Override
            public void onLocationsCanBeDeleted() {
                buttonEditLocations.setVisibility(View.GONE);
            }

            @Override
            public void onLocationsCannotBeDeleted() {
                buttonEditLocations.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDeletionModeChanged(boolean newValue) {
                if (newValue) {
                    layoutMenu.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blur));
                } else {
                    layoutMenu.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
                }
            }
        };
    }

}

