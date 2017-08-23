package com.example.julia.weatherguide.data.entities.presentation.location;


public class LocationPrediction {

    public final String placeId;

    public final String mainText;

    public final String secondaryText;

    public LocationPrediction(String placeId, String mainText, String secondaryText) {
        this.placeId = placeId;
        this.mainText = mainText;
        this.secondaryText = secondaryText;
    }

}
