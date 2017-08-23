package com.example.julia.weatherguide.presentation.menu;

import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithTemperature;

import java.util.List;


public interface LocationModel {

    void attachCallbacks(Callbacks callbacks);

    void detachCallbacks();


    void setupLocations(List<LocationWithTemperature> locations);


    void setDeletionMode(boolean newValue);

    boolean isInDeletionMode();


    interface Callbacks {

        void onLocationClicked(LocationWithTemperature location);

        void onLocationRemoveClicked(LocationWithTemperature location);

        void onLocationsCanBeDeleted();

        void onLocationsCannotBeDeleted();

        void onDeletionModeChanged(boolean newValue);

    }
}