package com.example.julia.weatherguide.data.data_services.network.weather;

import com.example.julia.weatherguide.data.contants.OpenWeatherMapEndpoints;
import com.example.julia.weatherguide.data.contracts.network.OpenWeatherMapContract;
import com.example.julia.weatherguide.data.data_services.network.base.BaseNetworkService;
import com.example.julia.weatherguide.data.entities.repository.Location;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.entities.remote.WeatherInCity;

import java.io.IOException;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;

public class OpenWeatherMapService extends BaseNetworkService<OpenWeatherMapContract>
    implements NetworkWeatherService {

    public OpenWeatherMapService(String apiKey, OkHttpClient okHttpClient) {
        super(OpenWeatherMapEndpoints.OPEN_WEATHER_MAP_BASE_URL, apiKey, okHttpClient);
    }

    @Override
    protected OpenWeatherMapContract createService(Retrofit retrofit) {
        return retrofit.create(OpenWeatherMapContract.class);
    }

    // --------------------------------------- public -----------------------------------------------

    @Override
    public Single<WeatherInCity> getCurrentWeather(Location location) {
        if (location == null) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED));
        } else {
            return getService().getCurrentWeatherForLocation(location.latitude, location.longitude)
                .onErrorResumeNext(error ->
                    error instanceof IOException
                        ? Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE))
                        : Single.error(error)
                );
        }
    }
}
