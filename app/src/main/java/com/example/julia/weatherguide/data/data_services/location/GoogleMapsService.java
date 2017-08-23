package com.example.julia.weatherguide.data.data_services.location;

import com.example.julia.weatherguide.data.constants.GoogleMapsEndpoints;
import com.example.julia.weatherguide.data.contracts.network.location.GoogleMapsContract;
import com.example.julia.weatherguide.data.data_services.base.BaseNetworkService;
import com.example.julia.weatherguide.data.entities.network.location.coordinates.NetworkLocationCoordinates;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPrediction;
import com.example.julia.weatherguide.data.entities.network.location.predictions.NetworkLocationPredictions;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;

import static com.example.julia.weatherguide.data.constants.GoogleMapsEndpoints.VALUE_CITIES;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.API_ERROR;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;


public class GoogleMapsService extends BaseNetworkService<GoogleMapsContract>
    implements NetworkLocationService {

    public GoogleMapsService(OkHttpClient okHttpClient) {
        super(GoogleMapsEndpoints.OPEN_WEATHER_MAP_BASE_URL, okHttpClient);
    }

    // ----------------------------------- NetworkLocationService ---------------------------------

    @Override
    public Single<List<NetworkLocationPrediction>> getPredictionsForPhrase(String phrase) {
        return getService().getLocationPredictions(phrase, VALUE_CITIES)
            .map(NetworkLocationPredictions::getPredictions)
            .onErrorResumeNext(this::handleError);
    }

    @Override
    public Single<NetworkLocationCoordinates> getLocationCoordinates(NetworkLocationPrediction prediction) {
        return getService().getLocationCoordinates(prediction.getPlaceId())
            .onErrorResumeNext(this::handleError);
    }

    // ------------------------------------- BaseNetworkService -----------------------------------

    @Override
    protected GoogleMapsContract createService(Retrofit retrofit) {
        return retrofit.create(GoogleMapsContract.class);
    }

    // ------------------------------------------ private -----------------------------------------

    private Single handleError(Throwable error) {
        if (error instanceof HttpException) return Single.error(new ExceptionBundle(API_ERROR));
        if (error instanceof IOException)
            return Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE));
        return Single.error(error);
    }
}
