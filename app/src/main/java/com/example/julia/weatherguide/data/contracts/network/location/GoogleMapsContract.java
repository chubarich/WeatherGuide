package com.example.julia.weatherguide.data.contracts.network.location;

import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationCoordinates;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPredictions;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import static com.example.julia.weatherguide.data.constants.GoogleMapsEndpoints.*;


public interface GoogleMapsContract {

    @GET(MAPS + API + PLACE + DETAILS + JSON)
    Single<NetworkLocationCoordinates> getLocationCoordinates(
        @Query(KEY_PLACEID) String placeId
    );

    @GET(MAPS + API + PLACE + AUTOCOMPLETE + JSON)
    Single<NetworkLocationPredictions> getLocationPredictions(
        @Query(KEY_INPUT) String phrase,
        @Query(KEY_TYPES) String types
    );

}
