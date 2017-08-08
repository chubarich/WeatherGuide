package com.example.julia.weatherguide.data.converters.location;

import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationCoordinates;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPrediction;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.repository.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;


public interface LocationConverter {

    LocationWithId fromDatabase(DatabaseLocation location, long currentLocationId);


    LocationPrediction fromNetwork(NetworkLocationPrediction locationPrediction);

    Location fromNetwork(NetworkLocationCoordinates coordinates,
                         LocationPrediction prediction);


    DatabaseLocation toDatabase(Location location);

    DatabaseLocation toDatabase(Location location, String newName);

    DatabaseLocation toDatabase(NetworkLocationCoordinates coordinates,
                                LocationPrediction prediction);


    NetworkLocationPrediction toNetwork(LocationPrediction locationPrediction);

}
