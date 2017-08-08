package com.example.julia.weatherguide.presentation.menu;

import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;
import com.example.julia.weatherguide.presentation.base.view.BaseView;

import java.util.List;


public interface MenuView extends BaseView {

    void setLocationsToAdapter(List<LocationWithTemperature> locations);

    void removeLocationFromAdapter(LocationWithTemperature location);

    void addLocationToAdapter(LocationWithTemperature location);

    void showLocationNotAdded();

    void showLocationNotRemoved();

    void onDrawerClosed();

}
