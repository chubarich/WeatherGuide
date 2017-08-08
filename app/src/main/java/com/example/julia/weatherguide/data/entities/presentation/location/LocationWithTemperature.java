package com.example.julia.weatherguide.data.entities.presentation.location;

import com.example.julia.weatherguide.utils.Preconditions;


public class LocationWithTemperature {

    public final Location location;

    public final Double temperature;

    public LocationWithTemperature(Location location, Double temperature) {
        Preconditions.nonNull(location);
        this.location = location;
        this.temperature = temperature;
    }
}

