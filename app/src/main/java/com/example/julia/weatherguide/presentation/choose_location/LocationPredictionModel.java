package com.example.julia.weatherguide.presentation.choose_location;

import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;

import java.util.List;


public interface LocationPredictionModel {

    void setPredictions(List<LocationPrediction> locationPredictions);

    void attachCallbacks(Callbacks callbacks);

    void detachCallbacks();


    interface Callbacks {

        void onPredictionClickListener(LocationPrediction locationPrediction);

    }
}
