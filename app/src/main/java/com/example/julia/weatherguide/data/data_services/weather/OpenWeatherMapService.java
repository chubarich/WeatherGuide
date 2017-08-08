package com.example.julia.weatherguide.data.data_services.weather;

import com.example.julia.weatherguide.data.constants.OpenWeatherMapEndpoints;
import com.example.julia.weatherguide.data.contracts.network.weather.OpenWeatherMapContract;
import com.example.julia.weatherguide.data.data_services.base.BaseNetworkService;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkWeatherPredictions;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkWeatherPrediction;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkCurrentWeather;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.API_ERROR;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;


public class OpenWeatherMapService extends BaseNetworkService<OpenWeatherMapContract>
    implements NetworkWeatherService {

    public OpenWeatherMapService(OkHttpClient okHttpClient) {
        super(OpenWeatherMapEndpoints.OPEN_WEATHER_MAP_BASE_URL, okHttpClient);
    }

    @Override
    protected OpenWeatherMapContract createService(Retrofit retrofit) {
        return retrofit.create(OpenWeatherMapContract.class);
    }

    // --------------------------------------- public -----------------------------------------------

    @Override
    public Single<NetworkCurrentWeather> getCurrentWeather(float latitude, float longitude) {
        return getService().getCurrentWeather(latitude, longitude)
            .doOnSuccess(networkCurrentWeather -> {
                if (networkCurrentWeather.getCode() != 200) throw new ExceptionBundle(API_ERROR);
            })
            .onErrorResumeNext(error ->
                error instanceof HttpException
                    ? Single.error(new ExceptionBundle(API_ERROR))
                    : (error instanceof IOException
                    ? Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE))
                    : Single.error(error)
                )
            );
    }

    @Override
    public Single<List<NetworkWeatherPrediction>> getPredictions(float latitude, float longitude, int count) {
        return getService().getWeatherPrediction(latitude, longitude, count)
            .doOnSuccess(prediction -> {
                if (prediction.getCode() != 200) throw new ExceptionBundle(API_ERROR);
            })
            .map(NetworkWeatherPredictions::getPredictions)
            .onErrorResumeNext(error ->
                error instanceof HttpException
                    ? Single.error(new ExceptionBundle(API_ERROR))
                    : (error instanceof IOException
                    ? Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE))
                    : Single.error(error)
                )
            );
    }
}
