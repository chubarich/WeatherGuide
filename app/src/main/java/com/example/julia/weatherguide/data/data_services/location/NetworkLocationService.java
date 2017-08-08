package com.example.julia.weatherguide.data.data_services.location;

import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationCoordinates;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPrediction;

import java.util.List;
import io.reactivex.Single;


public interface NetworkLocationService {

    Single<List<NetworkLocationPrediction>> getPredictionsForPhrase(String phrase);

    Single<NetworkLocationCoordinates> getLocationCoordinates(NetworkLocationPrediction prediction);

}
