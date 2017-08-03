package com.example.julia.weatherguide.data.repositories.weather;

import com.example.julia.weatherguide.data.data_services.local.weather.LocalWeatherService;
import com.example.julia.weatherguide.data.data_services.network.weather.NetworkWeatherService;
import com.example.julia.weatherguide.data.entities.repository.Location;
import com.example.julia.weatherguide.data.entities.repository.WeatherDataModel;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.entities.remote.WeatherInCity;
import com.example.julia.weatherguide.data.data_services.local.settings.SettingsService;
import com.squareup.picasso.Picasso;

import io.reactivex.Completable;
import io.reactivex.Single;


public class OpenWeatherMapRepository implements WeatherRepository {

    private final LocalWeatherService localWeatherService;
    private final NetworkWeatherService networkWeatherService;

    public OpenWeatherMapRepository(LocalWeatherService localWeatherService,
                                    NetworkWeatherService networkWeatherService) {
        this.localWeatherService = localWeatherService;
        this.networkWeatherService = networkWeatherService;
    }

    @Override
    public Single<WeatherDataModel> getWeather() {
        if (!isLocationInitialized()) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED));
        } else {
            return getFreshWeather().onErrorResumeNext(
                throwable -> localWeatherService.getWeather()
            );
        }
    }

    @Override
    public Single<WeatherDataModel> getFreshWeather() {
        if (!isLocationInitialized()) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED));
        } else {
            return networkWeatherService.getCurrentWeather(localWeatherService.getCurrentLocation())
                .map(this::getWeatherDataModelFromNetwork)
                .flatMap(weatherDataModel ->
                    settingsService.saveWeatherForCurrentLocation(weatherDataModel)
                        .onErrorComplete()
                        .toSingle(() -> {
                            weatherDataModel.setLocationName(settingsService.getCurrentLocationName());
                            return weatherDataModel;
                        })
                );
        }
    }

    @Override
    public Completable saveCurrentLocation(final Location location, final String cityName) {
        return settingsService.addLocation(location, cityName);
    }

    @Override
    public boolean isLocationInitialized() {
        return settingsService.isLocationInitialized();
    }


    private WeatherDataModel getWeatherDataModelFromNetwork(WeatherInCity weatherInCity) {
        WeatherDataModel data = new WeatherDataModel();
        data.setLocationName(weatherInCity.getName());
        data.setCurrentTemperature(String.valueOf(weatherInCity.getMain().getTemp()));
        data.setHumidity(weatherInCity.getMain().getHumidity());
        data.setIconId(weatherInCity.getWeather().get(0).getIcon());
        data.setWeatherDescription(weatherInCity.getWeather().get(0).getDescription());


        return data;
    }
}
