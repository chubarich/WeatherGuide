package com.example.julia.weatherguide.repositories.network;


import com.example.julia.weatherguide.data.data_services.network.weather.OpenWeatherMapService;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OpenWeatherMapNetworkServiceTest {
    /*
    @Test
    public void getCurrentWeather_throwsExceptionWhenLocationIsNull() {
        OpenWeatherMapService networkService = new OpenWeatherMapService();
        networkService.getCurrentWeather(null)
            .test()
            .assertError(
                error -> error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason()
                    == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );
    }
    */

}
