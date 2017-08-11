package com.example.julia.weatherguide.data.entities.presentation.location;

import com.example.julia.weatherguide.utils.Preconditions;


public class LocationWithTemperature {

    public final Location location;

    public final Integer temperature;

    public LocationWithTemperature(Location location, Integer temperature) {
        Preconditions.nonNull(location);
        this.location = location;
        this.temperature = temperature;
    }

    public boolean hasTheSameData(LocationWithTemperature other) {
        if (other == null) return false;

        if (this.temperature == null) {
            if (other.temperature != null) return false;
        } else {
            if (!this.temperature.equals(other.temperature)) return false;
        }

        return this.location.hasTheSameData(other.location);
    }
}

