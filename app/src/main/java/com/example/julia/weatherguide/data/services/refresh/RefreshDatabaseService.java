package com.example.julia.weatherguide.data.services.refresh;

import android.app.IntentService;
import android.content.Intent;

import com.example.julia.weatherguide.data.data_services.settings.SettingsService;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.presentation.application.WeatherGuideApplication;
import com.example.julia.weatherguide.utils.Optional;

import javax.inject.Inject;

import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy.FROM_NETWORK;


public class RefreshDatabaseService extends IntentService {

    public static final String KEY_CLASS_NAME = RefreshDatabaseService.class.getSimpleName();

    @Inject
    WeatherRepository weatherRepository;
    @Inject
    SettingsService settingsService;
    @Inject
    LocationRepository locationRepository;

    public RefreshDatabaseService() {
        super(KEY_CLASS_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((WeatherGuideApplication) getApplication()).getAppComponent()
            .inject(this);
    }

    @Override
    public void onDestroy() {
        weatherRepository = null;
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Long locationId = settingsService.getCurrentLocationId();
        if (locationId != -1) {
            Optional<LocationWithId> locationWithId = locationRepository
                .subscribeOnCurrentLocationChanges()
                .onErrorReturn(t -> Optional.of(null))
                .blockingFirst();
            if (locationWithId.isPresent()) {
                weatherRepository.getWeather(locationWithId.get(), FROM_NETWORK)
                    .subscribe(weather -> {}, error -> {});
            }
        }
    }
}