package com.example.julia.weatherguide.presentation.menu;

import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;

import java.util.List;


public interface LocationModel {

    void attachCallbacks(Callbacks callbacks);

    void detachCallbacks();


    void setupLocations(List<LocationWithTemperature> locations);

    void removeLocation(LocationWithTemperature location);

    void addLocation(LocationWithTemperature location);


    void setDeletionMode(boolean newValue);

    boolean isDeletionMode();


    interface Callbacks {

        void onLocationClicked(LocationWithTemperature location);

        void onLocationRemoveClicked(LocationWithTemperature location);

        void onLocationsEmpty();

        void onLocationsNotEmpty();

        void onDeletionModeChanged(boolean newValue);

    }
}