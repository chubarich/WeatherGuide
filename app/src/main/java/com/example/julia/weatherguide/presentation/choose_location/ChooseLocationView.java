package com.example.julia.weatherguide.presentation.choose_location;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.presentation.base.view.BaseView;

import java.util.List;


public interface ChooseLocationView extends BaseView {

    void hideProgressBar();

    void showNoInternet(boolean asOverlay);

    void showResults(List<LocationPrediction> locationPredictions, String request);

    void finishLocationChoosing(Location location);
}
