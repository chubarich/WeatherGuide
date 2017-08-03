package com.example.julia.weatherguide.repositories;

import com.example.julia.weatherguide.data.data_services.local.settings.SettingsService;
import com.example.julia.weatherguide.data.data_services.network.weather.NetworkWeatherService;
import com.example.julia.weatherguide.data.entities.repository.Location;
import com.example.julia.weatherguide.data.entities.repository.WeatherDataModel;
import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;
import com.example.julia.weatherguide.data.entities.remote.Main;
import com.example.julia.weatherguide.data.entities.remote.Weather;
import com.example.julia.weatherguide.data.entities.remote.WeatherInCity;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository;
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

import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.EMPTY_DATABASE;
import static com.example.julia.weatherguide.data.exceptions.ExceptionBundle.Reason.NETWORK_UNAVAILABLE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class WeatherRepositoryTest {

    private static final Location LOCATION = new Location(1.5f, 2.0f);
    private static final String NETWORK_DESCRIPTION = "NETWORK";
    private static final double NETWORK_TEMPERATURE = 22.5;
    private static final String NETWORK_LOCATION_NAME = "unused";
    private static final String DATABASE_DESCRIPTION = "DATABASE";
    private static final double DATABASE_TEMPERATURE = 2567.2;
    private static final String DATABASE_LOCATION_NAME = "default city";

    private WeatherRepository weatherRepository;
    private SettingsService settingsService;
    private NetworkWeatherService networkWeatherService;

    @Before
    public void before() throws Exception {
        settingsService = mock(SettingsService.class);
        networkWeatherService = mock(NetworkWeatherService.class);
        Picasso picasso = mock(Picasso.class);
        RequestCreator requestCreator = mock(RequestCreator.class);

        when(picasso.load(anyString())).thenReturn(requestCreator);
        when(requestCreator.get()).thenThrow(new IOException());
        when(settingsService.saveWeatherForCurrentLocation(any(WeatherDataModel.class)))
            .thenReturn(Completable.fromAction(() -> {
            }));

        weatherRepository = new OpenWeatherMapRepository(
            settingsService,
            networkWeatherService,
            picasso
        );
    }


    // When location is not initialized, LOCATION_NOT_INITIALIZED
    // exception must be thrown from both methods

    @Test
    public void getCurrentWeather_notInitialized() throws Exception {
        setLocationNotInitialized();

        weatherRepository.getWeather()
            .test()
            .assertError(error ->
                error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason()
                    == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );
        Mockito.verify(settingsService, never()).getWeather();
        Mockito.verify(networkWeatherService, never()).getCurrentWeather(any(Location.class));
    }

    @Test
    public void getFreshCurrentWeather_notInitialized() throws Exception {
        setLocationNotInitialized();

        weatherRepository.getFreshWeather()
            .test()
            .assertError(error ->
                error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason()
                    == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );
        Mockito.verify(settingsService, never()).getWeather();
        Mockito.verify(networkWeatherService, never()).getCurrentWeather(any(Location.class));
    }


    // When we have internet connection, we want to see data from there

    @Test
    public void getCurrentWeather_hasInternet() throws Exception {
        setLocationInitialized();
        when(networkWeatherService.getCurrentWeather(any(Location.class)))
            .thenReturn(Single.just(getDummyNetworkWeather()));
        when(settingsService.getWeather())
            .thenReturn(Single.just(getDummyDatabaseWeather()));

        weatherRepository.getWeather()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(weatherDataModel ->
                weatherDataModel.getLocationName().equals(DATABASE_LOCATION_NAME)
                    && weatherDataModel.getWeatherDescription().equals(NETWORK_DESCRIPTION)
                    && weatherDataModel.getCurrentTemperature().equals(String.valueOf(NETWORK_TEMPERATURE))
            );
        Mockito.verify(networkWeatherService, Mockito.times(1)).getCurrentWeather(any(Location.class));
        Mockito.verify(settingsService, never()).getWeather();
    }

    @Test
    public void getFreshCurrentWeather_hasInternet() throws Exception {
        setLocationInitialized();
        when(networkWeatherService.getCurrentWeather(any(Location.class)))
            .thenReturn(Single.just(getDummyNetworkWeather()));
        when(settingsService.getWeather())
            .thenReturn(Single.just(getDummyDatabaseWeather()));

        weatherRepository.getFreshWeather()
            .test()
            .assertComplete()
            .assertValue(weatherDataModel ->
                weatherDataModel.getLocationName().equals(DATABASE_LOCATION_NAME)
                    && weatherDataModel.getWeatherDescription().equals(NETWORK_DESCRIPTION)
                    && weatherDataModel.getCurrentTemperature().equals(String.valueOf(NETWORK_TEMPERATURE))
            );
        verify(settingsService, never()).getWeather();
        verify(networkWeatherService, atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    // When we want to see fresh data and have no internet,
    // NETWORK_UNAVAILABLE exception must be thrown

    @Test
    public void getFreshCurrentWeather_noInternet() throws Exception {
        setLocationInitialized();
        when(networkWeatherService.getCurrentWeather(any(Location.class)))
            .thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));
        when(settingsService.getWeather())
            .thenReturn(Single.just(getDummyDatabaseWeather()));

        weatherRepository.getFreshWeather()
            .test()
            .assertError(error ->
                error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason() == NETWORK_UNAVAILABLE
            );
        verify(settingsService, never()).getWeather();
        verify(networkWeatherService, atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    // When we want to see any data and have nor internet nor db data,
    // EMPTY_DATABASE exception must be thrown

    @Test
    public void getCurrentWeather_noInternet_noDatabase() throws Exception {
        setLocationInitialized();
        when(networkWeatherService.getCurrentWeather(any(Location.class)))
            .thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));
        when(settingsService.getWeather())
            .thenReturn(Single.error(new ExceptionBundle(EMPTY_DATABASE)));

        weatherRepository.getWeather()
            .test()
            .assertError(error ->
                error instanceof ExceptionBundle
                    && ((ExceptionBundle) error).getReason() == EMPTY_DATABASE
            );
        verify(settingsService, atLeastOnce()).getWeather();
        verify(networkWeatherService, atLeastOnce()).getCurrentWeather(any(Location.class));
    }


    // When we want to see any data, have no internet, but have data in db,
    // data from db must be returned

    @Test
    public void getCurrentWeather_noInternet_hasInDatabase() throws Exception {
        setLocationInitialized();
        when(networkWeatherService.getCurrentWeather(LOCATION))
            .thenReturn(Single.error(new ExceptionBundle(NETWORK_UNAVAILABLE)));
        when(settingsService.getWeather())
            .thenReturn(Single.just(getDummyDatabaseWeather()));

        weatherRepository.getWeather()
            .test()
            .assertComplete()
            .assertValue(weatherDataModel ->
                weatherDataModel.getLocationName().equals(DATABASE_LOCATION_NAME)
                    && weatherDataModel.getWeatherDescription().equals(DATABASE_DESCRIPTION)
                    && weatherDataModel.getCurrentTemperature().equals(String.valueOf(DATABASE_TEMPERATURE))
            );
        verify(settingsService, atLeastOnce()).getWeather();
        verify(networkWeatherService, atLeastOnce()).getCurrentWeather(any(Location.class));
    }

    // --------------------------------------- private --------------------------------------------

    private void setLocationInitialized() {
        when(settingsService.getCurrentLocation()).thenReturn(LOCATION);
        when(settingsService.getCurrentLocationName()).thenReturn(DATABASE_LOCATION_NAME);
        when(settingsService.isLocationInitialized()).thenReturn(true);
    }

    private void setLocationNotInitialized() {
        when(settingsService.getCurrentLocation()).thenReturn(null);
        when(settingsService.isLocationInitialized()).thenReturn(false);
        when(settingsService.getCurrentLocationName()).thenReturn(null);
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
