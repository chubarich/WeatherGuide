package com.example.julia.weatherguide.data.converters.location;

import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationCoordinates;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPrediction;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationStructuredFormatting;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;


public class LocationConverterPlain implements LocationConverter {

    @Override
    public LocationWithId fromDatabase(DatabaseLocation location, Long currentLocationId) {
        if (location.getId() == null) {
            return null;
        } else {
            long locationId = location.getId();
            return new LocationWithId(
                location.getId(),
                new Location(location.getLongitude(),
                    location.getLatitude(),
                    location.getName(),
                    currentLocationId != null && locationId == currentLocationId
                )
            );
        }
    }

    @Override
    public LocationPrediction fromNetwork(NetworkLocationPrediction locationPrediction) {
        return new LocationPrediction(
            locationPrediction.getPlaceId(),
            locationPrediction.getMainText(),
            locationPrediction.getSecondaryText()
        );
    }

    @Override
    public DatabaseLocation toDatabase(Location location) {
        return new DatabaseLocation(location.longitude, location.latitude, location.name);
    }

    @Override
    public DatabaseLocation toDatabase(Location location, String newName) {
        return new DatabaseLocation(location.longitude, location.latitude, newName);
    }

    @Override
    public Location fromNetwork(NetworkLocationCoordinates coordinates, LocationPrediction prediction) {
        return new Location((float) coordinates.getLongitude(), (float) coordinates.getLatitude(),
            prediction.mainText);
    }

    @Override
    public NetworkLocationPrediction toNetwork(LocationPrediction locationPrediction) {
        NetworkLocationStructuredFormatting formatting = new NetworkLocationStructuredFormatting(
            locationPrediction.mainText,
            locationPrediction.secondaryText
        );
        return new NetworkLocationPrediction(locationPrediction.placeId, formatting);
    }

    @Override
    public DatabaseLocation toDatabase(NetworkLocationCoordinates coordinates, LocationPrediction prediction) {
        return new DatabaseLocation(coordinates.getLongitude(), coordinates.getLatitude(), prediction.mainText);
    }

}
