package com.example.julia.weatherguide.repositories.network;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;
import com.example.julia.weatherguide.repositories.network.weather_data.WeatherInCity;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.reactivex.Single;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherMapNetworkService implements NetworkService {

    private static final String BASE_URL = "http://api.openweathermap.org/";
    private static final String API_KEY = "1f68b1b61499afeb695fe6ac1b090082";
    private static final String METRIC_NAME = "metric";

    private final OpenWeatherMapAPI weatherApi;


    public OpenWeatherMapNetworkService() {
        this.weatherApi = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    new GsonBuilder()
                        .setLenient()
                        .create()
                )
            ).client(buildOkHttpClient(Locale.getDefault().getLanguage()))
            .build()
            .create(OpenWeatherMapAPI.class);
    }

    // --------------------------------------- public -----------------------------------------------

    @Override
    public Single<WeatherInCity> getCurrentWeather(Location location) {
        if (location == null) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED));
        } else {
            return weatherApi.getCurrentWeatherForLocation(location.latitude, location.longitude);
        }
    }

    // --------------------------------------- private ----------------------------------------------

    private OkHttpClient buildOkHttpClient(String currentLang) {
        return new OkHttpClient().newBuilder()
            .addInterceptor(
                (chain) -> {
                    HttpUrl url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("units", METRIC_NAME)
                        .addQueryParameter("lang", currentLang)
                        .addQueryParameter("APPID", API_KEY)
                        .build();
                    Request request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build();
                    return chain.proceed(request);
                }
            ).build();
    }
}
