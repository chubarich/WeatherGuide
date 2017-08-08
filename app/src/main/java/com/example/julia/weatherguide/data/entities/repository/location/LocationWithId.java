package com.example.julia.weatherguide.data.entities.repository.location;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.utils.Preconditions;


public class LocationWithId {

    public final long id;

    public final Location location;

    public LocationWithId(long id, Location location) {
        Preconditions.nonNull(location);
        this.id = id;
        this.location = location;
    }

}
