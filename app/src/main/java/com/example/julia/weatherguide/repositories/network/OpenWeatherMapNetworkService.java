package com.example.julia.weatherguide.repositories.network;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;
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
    private static final String ICON_URL_HEAD = "http://openweathermap.org/img/w/";
    private static final String ICON_URL_TAIL = ".png";
    private static final String API_KEY = "1f68b1b61499afeb695fe6ac1b090082";
    private static final String METRIC_NAME = "metric";

    private final Context applicationContext;
    private final OpenWeatherMapAPI weatherApi;


    public OpenWeatherMapNetworkService(Context applicationContext) {
        this.applicationContext = applicationContext;
        this.weatherApi = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(
                new GsonBuilder()
                    .setLenient()
                    .create())
            ).client(buildOkHttpClient(Locale.getDefault().getLanguage()))
            .build()
            .create(OpenWeatherMapAPI.class);
    }

    // --------------------------------------- public -----------------------------------------------

    @Override
    public Single<WeatherDataModel> getCurrentWeather(Location location) {
        if (location == null) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED));
        } else {
            return weatherApi.getCurrentWeatherForLocation(location.latitude, location.longitude)
                .map(weatherInCity -> {
                    WeatherDataModel data = new WeatherDataModel();
                    data.setLocationName(weatherInCity.getName());
                    data.setCurrentTemperature(String.valueOf(weatherInCity.getMain().getTemp()));
                    data.setHumidity(weatherInCity.getMain().getHumidity());
                    data.setIconId(weatherInCity.getWeather().get(0).getIcon());
                    data.setWeatherDescription(weatherInCity.getWeather().get(0).getDescription());

                    Bitmap bitmap = Picasso.with(applicationContext)
                        .load(ICON_URL_HEAD + data.getIconId() + ICON_URL_TAIL)
                        .get();
                    data.setIcon(bitmap);

                    return data;
                });
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
