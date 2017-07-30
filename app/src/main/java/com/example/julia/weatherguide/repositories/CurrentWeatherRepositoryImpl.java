package com.example.julia.weatherguide.repositories;

import android.graphics.Bitmap;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;
import com.example.julia.weatherguide.repositories.network.NetworkService;
import com.example.julia.weatherguide.repositories.network.weather_data.WeatherInCity;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class CurrentWeatherRepositoryImpl implements CurrentWeatherRepository {

    private static final String ICON_URL_HEAD = "http://openweathermap.org/img/w/";
    private static final String ICON_URL_TAIL = ".png";

    private final SharedPreferenceService sharedPreferenceService;
    private final NetworkService openWeatherMapNetworkService;
    private final Picasso picasso;

    public CurrentWeatherRepositoryImpl(SharedPreferenceService sharedPreferenceService,
                                        NetworkService networkService,
                                        Picasso picasso) {
        this.sharedPreferenceService = sharedPreferenceService;
        this.openWeatherMapNetworkService = networkService;
        this.picasso = picasso;
    }

    @Override
    public Single<WeatherDataModel> getCurrentWeather() {
        return getFreshCurrentWeather()
            .onErrorResumeNext(sharedPreferenceService.getCurrentWeather());
    }

    @Override
    public Single<WeatherDataModel> getFreshCurrentWeather() {
        if (!isLocationInitialized()) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED));
        } else {
            return openWeatherMapNetworkService.getCurrentWeather(sharedPreferenceService.getCurrentLocation())
                .map(this::getWeatherDataModelFromNetwork)
                .flatMap(weatherDataModel ->
                    sharedPreferenceService.saveWeatherForCurrentLocation(weatherDataModel)
                        .onErrorComplete()
                        .toSingle(() -> {
                            weatherDataModel.setLocationName(sharedPreferenceService.getCurrentLocationName());
                            return weatherDataModel;
                        })
                );
        }
    }

    @Override
    public Completable saveCurrentLocation(final Location location, final String cityName) {
        return sharedPreferenceService.saveCurrentLocation(location, cityName);
    }

    @Override
    public boolean isLocationInitialized() {
        return sharedPreferenceService.isLocationInitialized();
    }


    private WeatherDataModel getWeatherDataModelFromNetwork(WeatherInCity weatherInCity) {
        WeatherDataModel data = new WeatherDataModel();
        data.setLocationName(weatherInCity.getName());
        data.setCurrentTemperature(String.valueOf(weatherInCity.getMain().getTemp()));
        data.setHumidity(weatherInCity.getMain().getHumidity());
        data.setIconId(weatherInCity.getWeather().get(0).getIcon());
        data.setWeatherDescription(weatherInCity.getWeather().get(0).getDescription());

        Bitmap bitmap = null;
        try {
            bitmap = picasso.load(ICON_URL_HEAD + data.getIconId() + ICON_URL_TAIL).get();
        } catch (IOException e) {
            // do nothing
        } finally {
            data.setIcon(bitmap);
        }

        return data;
    }
}
