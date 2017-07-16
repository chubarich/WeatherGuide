package com.example.julia.weatherguide.services.current_weather_refresh;


import android.util.Log;
import android.widget.Toast;

import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepositoryImpl;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;



/**
 * Created by julia on 15.07.17.
 */

public class CurrentWeatherRefreshDataService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {

        CurrentWeatherRepository repository = new CurrentWeatherRepositoryImpl();
        repository.getFreshCurrentWeatherForLocation(repository.getCurrentLocation());
       // Toast.makeText(WeatherGuideApplication.getInstance().getApplicationContext(), "updated", Toast.LENGTH_LONG);
        jobFinished(job, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
