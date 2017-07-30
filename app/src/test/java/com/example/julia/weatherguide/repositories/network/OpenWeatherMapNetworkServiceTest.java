package com.example.julia.weatherguide.repositories.network;


import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OpenWeatherMapNetworkServiceTest {

    @Test
    public void getCurrentWeather_throwsExceptionWhenLocationIsNull() {
        OpenWeatherMapNetworkService networkService = new OpenWeatherMapNetworkService();
        networkService.getCurrentWeather(null)
            .test()
            .assertError(
                error -> error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason()
                    == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );
    }

}
