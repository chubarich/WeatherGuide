package com.example.julia.weatherguide.repositories.network;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by julia on 15.07.17.
 */

public class OpenWeatherMapNetworkService {

    private static final String TAG = OpenWeatherMapNetworkService.class.getSimpleName();
    private static final String BASE_URL = "http://api.openweathermap.org/";
    private static final String ICON_URL_HEAD = "http://openweathermap.org/img/w/";
    private static final String ICON_URL_TAIL = ".png";
    private static final String API_KEY = "1f68b1b61499afeb695fe6ac1b090082";
    private static final String METRIC_NAME = "metric";

    private static  OpenWeatherMapAPI weatherApi = null;
    private static  OpenWeatherMapNetworkService serviceInstance = null;

    protected OpenWeatherMapNetworkService() {
        String currentLang = Locale.getDefault().getLanguage();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("units", METRIC_NAME)
                        .addQueryParameter("lang", currentLang)
                        .addQueryParameter("APPID", API_KEY)
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        weatherApi = retrofit.create(OpenWeatherMapAPI.class);
    }

    public synchronized static OpenWeatherMapNetworkService getService() {
        if (serviceInstance == null) {
            serviceInstance = new OpenWeatherMapNetworkService();
        }
        return serviceInstance;
    }

    public Observable<CurrentWeatherDataModel> getCurrentWeather (@NonNull String location) {
        return weatherApi.getCurrentWeatherForLocation(location).map(weatherInCity -> {
            CurrentWeatherDataModel data = new CurrentWeatherDataModel();
            data.setLocationName(weatherInCity.getName());
            data.setLocationId(weatherInCity.getId().toString());
            data.setCurrentTemperature(String.valueOf(weatherInCity.getMain().getTemp()));
            data.setHumidity(weatherInCity.getMain().getHumidity());
            data.setIconId(weatherInCity.getWeather().get(0).getIcon());
            data.setWeatherDescription(weatherInCity.getWeather().get(0).getDescription());
            Bitmap bitmap = Picasso.with(WeatherGuideApplication.getInstance().getApplicationContext())
                    .load(ICON_URL_HEAD + data.getIconId() + ICON_URL_TAIL)
                    .get();
            data.setIcon(bitmap);
            return data;
        });
    }
}
