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

import static com.example.julia.weatherguide.repositories.exception.ExceptionBundle.Reason.EMPTY_DATABASE;
import static com.example.julia.weatherguide.repositories.exception.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CurrentWeatherRepositoryTest {

    private static final Location LOCATION = new Location(1.5f, 2.0f);
    private static final String NETWORK_DESCRIPTION = "NETWORK";
    private static final double NETWORK_TEMPERATURE = 22.5;
    private static final String NETWORK_LOCATION_NAME = "unused";
    private static final String DATABASE_DESCRIPTION = "DATABASE";
    private static final double DATABASE_TEMPERATURE = 2567.2;
    private static final String DATABASE_LOCATION_NAME = "default city";

    private CurrentWeatherRepository currentWeatherRepository;
    private SharedPreferenceService sharedPreferenceService;
    private NetworkService networkService;

    @Before
    public void before() throws Exception {
        sharedPreferenceService = mock(SharedPreferenceService.class);
        networkService = mock(NetworkService.class);
        Picasso picasso = mock(Picasso.class);
        RequestCreator requestCreator = mock(RequestCreator.class);

        when(picasso.load(anyString())).thenReturn(requestCreator);
        when(requestCreator.get()).thenThrow(new IOException());
        when(sharedPreferenceService.saveWeatherForCurrentLocation(any(WeatherDataModel.class)))
            .thenReturn(Completable.fromAction(() -> {
            }));

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
            .assertError(error ->
                error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason()
                    == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );
        Mockito.verify(sharedPreferenceService, never()).getCurrentWeather();
        Mockito.verify(networkService, never()).getCurrentWeather(any(Location.class));
    }

    @Test
    public void getFreshCurrentWeather_notInitialized() throws Exception {
        setLocationNotInitialized();

        currentWeatherRepository.getFreshCurrentWeather()
            .test()
            .assertError(error ->
                error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason()
                    == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );
        Mockito.verify(sharedPreferenceService, never()).getCurrentWeather();
        Mockito.verify(networkService, never()).getCurrentWeather(any(Location.class));
    }


    // When we have internet connection, we want to see data from there

    @Test
    public void getCurrentWeather_hasInternet() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(any(Location.class)))
            .thenReturn(Single.just(getDummyNetworkWeather()));
        when(sharedPreferenceService.getCurrentWeather())
            .thenReturn(Single.just(getDummyDatabaseWeather()));

        currentWeatherRepository.getCurrentWeather()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(weatherDataModel ->
                weatherDataModel.getLocationName().equals(DATABASE_LOCATION_NAME)
                    && weatherDataModel.getWeatherDescription().equals(NETWORK_DESCRIPTION)
                    && weatherDataModel.getCurrentTemperature().equals(String.valueOf(NETWORK_TEMPERATURE))
            );
        Mockito.verify(networkService, Mockito.times(1)).getCurrentWeather(any(Location.class));
        Mockito.verify(sharedPreferenceService, never()).getCurrentWeather();
    }

    @Test
    public void getFreshCurrentWeather_hasInternet() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(any(Location.class)))
            .thenReturn(Single.just(getDummyNetworkWeather()));
        when(sharedPreferenceService.getCurrentWeather())
            .thenReturn(Single.just(getDummyDatabaseWeather()));

        currentWeatherRepository.getFreshCurrentWeather()
            .test()
            .assertComplete()
            .assertValue(weatherDataModel ->
                weatherDataModel.getLocationName().equals(DATABASE_LOCATION_NAME)
                    && weatherDataModel.getWeatherDescription().equals(NETWORK_DESCRIPTION)
                    && weatherDataModel.getCurrentTemperature().equals(String.valueOf(NETWORK_TEMPERATURE))
            );
        verify(sharedPreferenceService, never()).getCurrentWeather();
        verify(networkService, atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    // When we want to see fresh data and have no internet,
    // NETWORK_UNAVAILABLE exception must be thrown

    @Test
    public void getFreshCurrentWeather_noInternet() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(any(Location.class)))
            .thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));
        when(sharedPreferenceService.getCurrentWeather())
            .thenReturn(Single.just(getDummyDatabaseWeather()));

        currentWeatherRepository.getFreshCurrentWeather()
            .test()
            .assertError(error ->
                error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason() == NETWORK_UNAVAILABLE
            );
        verify(sharedPreferenceService, never()).getCurrentWeather();
        verify(networkService, atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    // When we want to see any data and have nor internet nor db data,
    // EMPTY_DATABASE exception must be thrown

    @Test
    public void getCurrentWeather_noInternet_noDatabase() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(any(Location.class)))
            .thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));
        when(sharedPreferenceService.getCurrentWeather())
            .thenReturn(Single.error(new ExceptionBundle(EMPTY_DATABASE)));

        currentWeatherRepository.getCurrentWeather()
            .test()
            .assertError(error ->
                error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason() == EMPTY_DATABASE
            );
        verify(sharedPreferenceService, atLeastOnce()).getCurrentWeather();
        verify(networkService, atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    // When we want to see any data, have no internet, but have data in db,
    // data from db must be returned

    @Test
    public void getCurrentWeather_noInternet_hasInDatabase() throws Exception {
        setLocationInitialized();
        when(networkService.getCurrentWeather(LOCATION))
            .thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));
        when(sharedPreferenceService.getCurrentWeather())
            .thenReturn(Single.just(getDummyDatabaseWeather()));

        currentWeatherRepository.getCurrentWeather()
            .test()
            .assertComplete()
            .assertValue(weatherDataModel ->
                weatherDataModel.getLocationName().equals(DATABASE_LOCATION_NAME)
                    && weatherDataModel.getWeatherDescription().equals(DATABASE_DESCRIPTION)
                    && weatherDataModel.getCurrentTemperature().equals(String.valueOf(DATABASE_TEMPERATURE))
            );
        verify(sharedPreferenceService, atLeastOnce()).getCurrentWeather();
        verify(networkService, atLeastOnce()).getCurrentWeather(any(Location.class));
    }

    // --------------------------------------- private --------------------------------------------

    private void setLocationInitialized() {
        when(sharedPreferenceService.getCurrentLocation()).thenReturn(LOCATION);
        when(sharedPreferenceService.getCurrentLocationName()).thenReturn(DATABASE_LOCATION_NAME);
        when(sharedPreferenceService.isLocationInitialized()).thenReturn(true);
    }

    private void setLocationNotInitialized() {
        when(sharedPreferenceService.getCurrentLocation()).thenReturn(null);
        when(sharedPreferenceService.isLocationInitialized()).thenReturn(false);
        when(sharedPreferenceService.getCurrentLocationName()).thenReturn(null);
    }

    private WeatherDataModel getDummyDatabaseWeather() {
        WeatherDataModel dummyWeather = new WeatherDataModel();
        dummyWeather.setLocationName(DATABASE_LOCATION_NAME);
        dummyWeather.setCurrentTemperature(String.valueOf(DATABASE_TEMPERATURE));
        dummyWeather.setWeatherDescription(DATABASE_DESCRIPTION);
        return dummyWeather;
    }

    private WeatherInCity getDummyNetworkWeather() {
        Main main = new Main();
        main.setTemp(NETWORK_TEMPERATURE);
        main.setHumidity(1);

        Weather weather = new Weather();
        weather.setDescription(NETWORK_DESCRIPTION);
        weather.setIcon("");
        List<Weather> list = new ArrayList<>();
        list.add(weather);

        WeatherInCity weatherInCity = new WeatherInCity();
        weatherInCity.setMain(main);
        weatherInCity.setWeather(list);
        weatherInCity.setName(NETWORK_LOCATION_NAME);
        return weatherInCity;
    }
}
