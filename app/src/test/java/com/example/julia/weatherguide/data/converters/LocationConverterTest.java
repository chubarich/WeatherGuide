package com.example.julia.weatherguide.data.converters;

import com.example.julia.weatherguide.data.converters.location.LocationConverter;
import com.example.julia.weatherguide.data.converters.location.LocationConverterPlain;
import com.example.julia.weatherguide.data.entities.local.DatabaseLocation;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocation;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationCoordinates;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationGeometry;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationResult;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPrediction;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationStructuredFormatting;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationPrediction;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationConverterTest {

    private static final double EPSILON = 0;
    private static final double LATITUDE = 5.2241;
    private static final double LONGITUDE = 2.3124;
    private static final String MAIN_TEXT = "Moscow";
    private static final String PLACE_ID = "2skakdsakf";
    private static final String SECONDARY_TEXT = "Is the capital of Russia";

    private LocationConverter locationConverter;

    @Before
    public void before() {
        locationConverter = new LocationConverterPlain();
    }

    @Test
    public void fromDatabaseLocationWithId_returnNull() {
        DatabaseLocation databaseLocation = new DatabaseLocation(3f, 4f, "Moscow");

        LocationWithId location = locationConverter.fromDatabase(databaseLocation, null);

        assertNull(location);
    }

    @Test
    public void fromNetworkLocationPrediction_returnConsistent() {
        NetworkLocationStructuredFormatting formatting = new NetworkLocationStructuredFormatting("Лондон", "");
        NetworkLocationPrediction networkLocationPrediction = new NetworkLocationPrediction("", formatting);

        LocationPrediction locationPrediction = locationConverter.fromNetwork(networkLocationPrediction);

        assertTrue(locationPrediction.mainText.equals(networkLocationPrediction.getMainText()));
        assertTrue(locationPrediction.secondaryText.equals(networkLocationPrediction.getSecondaryText()));
        assertTrue(locationPrediction.placeId.equals(networkLocationPrediction.getPlaceId()));
    }

    @Test
    public void fromNetworkLocation_returnConsistent() {
        NetworkLocation networkLocation = new NetworkLocation(LATITUDE, LONGITUDE);
        NetworkLocationGeometry geometry = new NetworkLocationGeometry(networkLocation);
        NetworkLocationResult result = new NetworkLocationResult(geometry);
        NetworkLocationCoordinates coordinates = new NetworkLocationCoordinates(result);
        LocationPrediction locationPrediction = new LocationPrediction("", MAIN_TEXT, "");

        Location location = locationConverter.fromNetwork(coordinates, locationPrediction);

        assertEquals(location.latitude, LATITUDE, EPSILON);
        assertEquals(location.longitude, LONGITUDE, EPSILON);
        assertTrue(MAIN_TEXT.equals(location.name));
    }

    @Test
    public void toDatabaseLocation_returnConsistent() {
        Location location = new Location(LONGITUDE, LATITUDE, MAIN_TEXT);

        DatabaseLocation databaseLocation = locationConverter.toDatabase(location);

        assertEquals(databaseLocation.getLatitude(), LATITUDE, EPSILON);
        assertEquals(databaseLocation.getLongitude(), LONGITUDE, EPSILON);
        assertTrue(MAIN_TEXT.equals(databaseLocation.getName()));
    }

    @Test
    public void toDatabaseLocationWithNewName_returnConsistent() {
        Location location = new Location(LONGITUDE, LATITUDE, "");

        DatabaseLocation databaseLocation = locationConverter.toDatabase(location, MAIN_TEXT);

        assertEquals(databaseLocation.getLatitude(), LATITUDE, EPSILON);
        assertEquals(databaseLocation.getLongitude(), LONGITUDE, EPSILON);
        assertTrue(MAIN_TEXT.equals(databaseLocation.getName()));
    }

    @Test
    public void toDatabaseFromNetwork_returnConsistent() {
        NetworkLocation networkLocation = new NetworkLocation(LATITUDE, LONGITUDE);
        NetworkLocationGeometry geometry = new NetworkLocationGeometry(networkLocation);
        NetworkLocationResult result = new NetworkLocationResult(geometry);
        NetworkLocationCoordinates coordinates = new NetworkLocationCoordinates(result);
        LocationPrediction locationPrediction = new LocationPrediction(PLACE_ID,
                MAIN_TEXT, SECONDARY_TEXT);

        DatabaseLocation databaseLocation = locationConverter.toDatabase(coordinates, locationPrediction);


        assertEquals(LATITUDE, databaseLocation.getLatitude(), EPSILON);
        assertEquals(LONGITUDE, databaseLocation.getLongitude(), EPSILON);
        assertTrue(MAIN_TEXT.equals(databaseLocation.getName()));
    }

    @Test
    public void toNetworkLocationPrediction_returnConsistent() {
        LocationPrediction locationPrediction = new LocationPrediction(PLACE_ID,
                MAIN_TEXT, SECONDARY_TEXT);

        NetworkLocationPrediction networkLocationPrediction = locationConverter.toNetwork(locationPrediction);

        assertTrue(MAIN_TEXT.equals(networkLocationPrediction.getMainText()));
        assertTrue(SECONDARY_TEXT.equals(networkLocationPrediction.getSecondaryText()));
        assertTrue(MAIN_TEXT.equals(networkLocationPrediction.getMainText()));
    }

}
