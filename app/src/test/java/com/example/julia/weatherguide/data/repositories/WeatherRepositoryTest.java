package com.example.julia.weatherguide.data.repositories;

import com.example.julia.weatherguide.data.converters.weather.SharedPreferencesWeatherConverter;
import com.example.julia.weatherguide.data.converters.weather.WeatherConverter;
import com.example.julia.weatherguide.data.data_services.location.LocalLocationService;
import com.example.julia.weatherguide.data.data_services.weather.LocalWeatherService;
import com.example.julia.weatherguide.data.data_services.weather.NetworkWeatherService;
import com.example.julia.weatherguide.data.helpers.DatetimeHelper;
import com.example.julia.weatherguide.data.helpers.DatetimeHelperPlain;
import com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;

import org.junit.Before;

import static org.mockito.Mockito.mock;


public class WeatherRepositoryTest {

    private WeatherRepository weatherRepository;

    @Before
    public void before() {
        DatetimeHelper datetimeHelper = new DatetimeHelperPlain();
        WeatherConverter weatherConverter = mock(WeatherConverter.class);
        LocalWeatherService localService = mock(LocalWeatherService.class);
        NetworkWeatherService networkService = mock(NetworkWeatherService.class);

        weatherRepository = new OpenWeatherMapRepository(datetimeHelper, weatherConverter,
                localService, networkService);
    }


}
