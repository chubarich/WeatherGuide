package com.example.julia.weatherguide.repositories;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;
import com.example.julia.weatherguide.repositories.network.NetworkService;
import com.example.julia.weatherguide.repositories.network.weather_data.Main;
import com.example.julia.weatherguide.repositories.network.weather_data.Weather;
import com.example.julia.weatherguide.repositories.network.weather_data.WeatherInCity;
import com.example.julia.weatherguide.repositories.storage.preferences.SharedPreferenceService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.example.julia.weatherguide.repositories.exception.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CurrentWeatherRepositoryTest {

    private static final Location DUMMY_LOCATION = new Location(1.5f, 2.0f);
    private static final String DUMMY_NETWORK_DESCRIPTION = "NETWORK";
    private static final double DUMMY_NETWORK_TEMPERATURE = 22.5;
    private static final String DUMMY_NETWORK_LOCATION_NAME= "unused";
    private static final String DUMMY_DATABASE_DESCRIPTION = "DATABASE";
    private static final double DUMMY_DATABASE_TEMPERATURE = 2567.2;
    private static final String DUMMY_DATABASE_LOCATION_NAME = "default city";

    private CurrentWeatherRepositoryImpl currentWeatherRepository;
    private SharedPreferenceService sharedPreferenceService;
    private NetworkService networkService;
    private Picasso picasso;
    private RequestCreator requestCreator;

    @Before
    public void before() throws Exception {
        sharedPreferenceService = mock(SharedPreferenceService.class);
        networkService = mock(NetworkService.class);
        picasso = mock(Picasso.class);
        requestCreator = mock(RequestCreator.class);

        when(picasso.load(anyString())).thenReturn(requestCreator);
        when(requestCreator.get()).thenThrow(new IOException());
        when(sharedPreferenceService.saveWeatherForCurrentLocation(any(WeatherDataModel.class)))
            .thenReturn(Completable.fromAction(()->{}));

        currentWeatherRepository = new CurrentWeatherRepositoryImpl(
            sharedPreferenceService,
            networkService,
            picasso
        );
    }


    // When location is not initialized, LOCATION_NOT_INITIALIZED
    // exception must be thrown from both methods

    @Test
    public void getCurrentWeather_notInitialized() throws Exception {
        setLocationNotInitialized();

        currentWeatherRepository.getCurrentWeather()
            .test()
            .assertError(error -> error instanceof ExceptionBundle
                && ((ExceptionBundle) error).getReason()
                == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );

        Mockito.verify(sharedPreferenceService, Mockito.never()).getCurrentWeather();
        Mockito.verify(networkService, Mockito.never()).getCurrentWeather(any(Location.class));
    }

    @Test
    public void getFreshCurrentWeather_notInitialized() throws Exception {
        setLocationNotInitialized();

        currentWeatherRepository.getFreshCurrentWeather()
            .test()
            .assertError(error -> error instanceof ExceptionBundle
                && ((ExceptionBundle) error).getReason()
                == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );

        Mockito.verify(sharedPreferenceService, Mockito.never()).getCurrentWeather();
        Mockito.verify(networkService, Mockito.never()).getCurrentWeather(any(Location.class));
    }


    // When we have internet connection, we want to see data from there

    @Test
    public void getCurrentWeather_hasInternet() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(any(Location.class))).thenReturn(
            Single.just(getDummyNetworkWeather())
        );
        when(sharedPreferenceService.getCurrentWeather()).thenReturn(
            Single.just(getDummyDatabaseWeather())
        );

        currentWeatherRepository.getCurrentWeather()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(weatherDataModel -> weatherDataModel.getLocationName().equals(DUMMY_DATABASE_LOCATION_NAME)
                && weatherDataModel.getWeatherDescription().equals(DUMMY_NETWORK_DESCRIPTION)
                && weatherDataModel.getCurrentTemperature().equals(String.valueOf(DUMMY_NETWORK_TEMPERATURE))
            );

        Mockito.verify(networkService, Mockito.times(1)).getCurrentWeather(any(Location.class));
        Mockito.verify(sharedPreferenceService, Mockito.never()).getCurrentWeather();
    }

    @Test
    public void getFreshCurrentWeather_hasInternet() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(any(Location.class))).thenReturn(
            Single.just(getDummyNetworkWeather())
        );
        when(sharedPreferenceService.getCurrentWeather()).thenReturn(
            Single.just(getDummyDatabaseWeather())
        );

       currentWeatherRepository.getFreshCurrentWeather()
            .test()
            .assertComplete()
            .assertValue(weatherDataModel ->
                weatherDataModel.getLocationName().equals(DUMMY_DATABASE_LOCATION_NAME)
                && weatherDataModel.getWeatherDescription().equals(DUMMY_NETWORK_DESCRIPTION)
                && weatherDataModel.getCurrentTemperature().equals(String.valueOf(DUMMY_NETWORK_TEMPERATURE))
            );

        Mockito.verify(sharedPreferenceService, Mockito.never()).getCurrentWeather();
        Mockito.verify(networkService, Mockito.atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    // When we want to see fresh data and have no internet,
    // NETWORK_UNAVAILABLE exception must be thrown

    @Test
    public void getFreshCurrentWeather_noInternet() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(any(Location.class))).thenReturn(
            Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE))
        );
        when(sharedPreferenceService.getCurrentWeather()).thenReturn(
            Single.just(getDummyDatabaseWeather())
        );

        currentWeatherRepository.getFreshCurrentWeather()
            .test()
            .assertError(error -> error instanceof ExceptionBundle
                && ((ExceptionBundle) error).getReason() == NETWORK_UNAVAILABLE
            );

        Mockito.verify(sharedPreferenceService, Mockito.never()).getCurrentWeather();
        Mockito.verify(networkService, Mockito.atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    // When we want to see any data and have nor internet nor db data,
    // EMPTY_DATABASE exception must be thrown

    @Test
    public void getCurrentWeather_noInternet_noDatabase() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(any(Location.class))).thenReturn(
            Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE))
        );
        when(sharedPreferenceService.getCurrentWeather()).thenReturn(
            Single.error(new ExceptionBundle(ExceptionBundle.Reason.EMPTY_DATABASE))
        );

        currentWeatherRepository.getCurrentWeather()
            .test()
            .assertError(error -> error instanceof ExceptionBundle
                && ((ExceptionBundle) error).getReason()
                == ExceptionBundle.Reason.EMPTY_DATABASE
            );

        Mockito.verify(sharedPreferenceService, Mockito.atLeastOnce()).getCurrentWeather();
        Mockito.verify(networkService, Mockito.atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    // When we want to see any data, have no internet, but have data in db,
    // data from db must be returned

    @Test
    public void getCurrentWeather_noInternet_hasInDatabase() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(DUMMY_LOCATION)).thenReturn(
            Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE))
        );
        when(sharedPreferenceService.getCurrentWeather()).thenReturn(
            Single.just(getDummyDatabaseWeather())
        );

        currentWeatherRepository.getCurrentWeather()
            .test()
            .assertComplete()
            .assertValue(weatherDataModel -> weatherDataModel.getLocationName().equals(DUMMY_DATABASE_LOCATION_NAME)
                && weatherDataModel.getWeatherDescription().equals(DUMMY_DATABASE_DESCRIPTION)
                && weatherDataModel.getCurrentTemperature().equals(String.valueOf(DUMMY_DATABASE_TEMPERATURE))
            );

        Mockito.verify(sharedPreferenceService, Mockito.atLeastOnce()).getCurrentWeather();
        Mockito.verify(networkService, Mockito.atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    private void setLocationInitialized() {
        when(sharedPreferenceService.getCurrentLocation()).thenReturn(DUMMY_LOCATION);
        when(sharedPreferenceService.getCurrentLocationName()).thenReturn(DUMMY_DATABASE_LOCATION_NAME);
        when(sharedPreferenceService.isLocationInitialized()).thenReturn(true);
    }

    private void setLocationNotInitialized() {
        when(sharedPreferenceService.getCurrentLocation()).thenReturn(null);
        when(sharedPreferenceService.isLocationInitialized()).thenReturn(false);
        when(sharedPreferenceService.getCurrentLocationName()).thenReturn(null);
    }

    private WeatherDataModel getDummyDatabaseWeather() {
        WeatherDataModel dummyWeather = new WeatherDataModel();
        dummyWeather.setLocationName(DUMMY_DATABASE_LOCATION_NAME);
        dummyWeather.setCurrentTemperature(String.valueOf(DUMMY_DATABASE_TEMPERATURE));
        dummyWeather.setWeatherDescription(DUMMY_DATABASE_DESCRIPTION);
        return dummyWeather;
    }

    private WeatherInCity getDummyNetworkWeather() {
        Main main = new Main();
        main.setTemp(DUMMY_NETWORK_TEMPERATURE);
        main.setHumidity(1);

        Weather weather = new Weather();
        weather.setDescription(DUMMY_NETWORK_DESCRIPTION);
        weather.setIcon("");
        List<Weather> list = new ArrayList<>();
        list.add(weather);

        WeatherInCity weatherInCity = new WeatherInCity();
        weatherInCity.setMain(main);
        weatherInCity.setWeather(list);
        weatherInCity.setName(DUMMY_NETWORK_LOCATION_NAME);
        return weatherInCity;
    }
}
