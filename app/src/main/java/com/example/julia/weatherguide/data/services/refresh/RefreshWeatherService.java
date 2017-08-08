package com.example.julia.weatherguide.data.services.refresh;

import android.util.Log;

import com.example.julia.weatherguide.data.entities.repository.location.LocationWithId;
import com.example.julia.weatherguide.data.repositories.location.LocationRepository;
import com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.presentation.application.WeatherGuideApplication;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy.FROM_NETWORK;

public class RefreshWeatherService extends JobService {

    private final static String TAG = RefreshWeatherService.class.getSimpleName();

    @Inject
    WeatherRepository weatherRepository;
    @Inject
    LocationRepository locationRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        ((WeatherGuideApplication) getApplication()).getAppComponent()
            .inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        List<LocationWithId> locations = locationRepository.getLocations().blockingGet();

        for (LocationWithId location : locations) {
            weatherRepository.getWeather(location, FROM_NETWORK)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    data -> {
                        jobFinished(job, false);
                        Log.d(TAG, "onStartJob get data ");
                    },
                    error -> Log.d(TAG, "onStartJob error " + error.getLocalizedMessage())
                );
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

}
