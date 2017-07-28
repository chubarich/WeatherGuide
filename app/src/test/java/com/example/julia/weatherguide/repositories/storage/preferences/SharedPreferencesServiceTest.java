package com.example.julia.weatherguide.repositories.storage.preferences;

import android.content.SharedPreferences;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class SharedPreferencesServiceTest {

    private static final double FLOAT_DELTA = 0.001f;

    private static final String SHARED_PREFERENCES_DUMMY_NAME = "SHARED_PREFERENCES_DUMMY_NAME";
    private static final String DUMMY_LOCATION_NAME = "default city";
    private static final float DUMMY_LATITUDE = 1.21f;
    private static final float DUMMY_LONGITUDE = 1.5f;
    private static final Location DUMMY_LOCATION = new Location(DUMMY_LONGITUDE, DUMMY_LATITUDE);


    private SharedPreferences sharedPreferences;
    private SharedPreferenceServiceImpl sharedPreferenceService;

    @Before
    public void before() throws Exception {
        sharedPreferences = RuntimeEnvironment.application
            .getSharedPreferences(SHARED_PREFERENCES_DUMMY_NAME, 0);
        sharedPreferences.edit().clear().apply();
        sharedPreferenceService = new SharedPreferenceServiceImpl(sharedPreferences);
    }

    @Test
    public void preferencesAndServiceAreNonNull() {
        assertNotNull(sharedPreferences);
        assertNotNull(sharedPreferenceService);
    }


    @Test
    public void isLocationInitialized_returnFalseWhenNotInitialized() throws Exception {
        assertFalse(sharedPreferenceService.isLocationInitialized());
    }

    @Test
    public void getCurrentLocation_returnNullWhenNotInitialized() throws Exception {
        assertNull(sharedPreferenceService.getCurrentLocation());
    }

    @Test
    public void getCurrentLocationName_returnNullWhenNotInitialized() throws Exception {
        assertNull(sharedPreferenceService.getCurrentLocationName());
    }


    @Test
    public void saveLocation_isLocationInitialized_saveAndReturnTrue() throws Exception {
        sharedPreferenceService.saveCurrentLocation(DUMMY_LOCATION, DUMMY_LOCATION_NAME).blockingAwait();

        assertTrue(sharedPreferenceService.isLocationInitialized());
    }

    @Test
    public void saveLocation_getLocationName_saveAndReturnExpected() throws Exception {
        sharedPreferenceService.saveCurrentLocation(DUMMY_LOCATION, DUMMY_LOCATION_NAME).blockingAwait();

        assertEquals(sharedPreferenceService.getCurrentLocationName(), DUMMY_LOCATION_NAME);
    }

    @Test
    public void saveLocation_getLocation_saveAndReturnExpected() throws Exception {
        sharedPreferenceService.saveCurrentLocation(DUMMY_LOCATION, DUMMY_LOCATION_NAME).blockingAwait();

        Location location = sharedPreferenceService.getCurrentLocation();
        assertNotNull(location);
        assertEquals(location.latitude, DUMMY_LATITUDE, FLOAT_DELTA);
        assertEquals(location.longitude, DUMMY_LONGITUDE, FLOAT_DELTA);
    }


    @Test
    public void saveWeather_throwsLocationNotInitialized() throws Exception {
        sharedPreferenceService.saveWeatherForCurrentLocation(getDummyWeather())
            .test()
            .assertError(throwable ->
                throwable instanceof ExceptionBundle
                    && ((ExceptionBundle) throwable).getReason()
                    == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );
    }


    @Test
    public void getWeather_throwsLocationNotInitialized() throws Exception {
        sharedPreferenceService.getCurrentWeather()
            .test()
            .assertError(throwable ->
                (throwable instanceof ExceptionBundle)
                    && ((ExceptionBundle) throwable).getReason()
                    == ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED
            );
    }

    @Test
    public void saveLocation_getWeather_throwsEmptyDatabase() throws Exception {
        sharedPreferenceService.saveCurrentLocation(DUMMY_LOCATION, DUMMY_LOCATION_NAME).blockingAwait();

        sharedPreferenceService.getCurrentWeather()
            .test()
            .assertError(throwable ->
                (throwable instanceof ExceptionBundle)
                    && ((ExceptionBundle) throwable).getReason()
                    == ExceptionBundle.Reason.EMPTY_DATABASE
            );
    }

    @Test
    public void saveLocation_saveWeather_getWeather_returnsExpected() throws Exception {
        sharedPreferenceService.saveCurrentLocation(DUMMY_LOCATION, DUMMY_LOCATION_NAME).blockingAwait();

        WeatherDataModel dummyWeather = getDummyWeather();
        sharedPreferenceService.saveWeatherForCurrentLocation(dummyWeather).blockingAwait();

        sharedPreferenceService.getCurrentWeather()
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(weatherDataModel ->
                weatherDataModel.getHumidity() == dummyWeather.getHumidity()
                    && weatherDataModel.getIconId().equals(dummyWeather.getIconId())
                    && weatherDataModel.getLocationName().equals(dummyWeather.getLocationName())
                    && weatherDataModel.getWeatherDescription() != null
                    && weatherDataModel.getCurrentTemperature() != null
            );
    }


    // -------------------------------------- private ---------------------------------------------

    private WeatherDataModel getDummyWeather() {
        WeatherDataModel dummyWeather = new WeatherDataModel();
        dummyWeather.setLocationName(DUMMY_LOCATION_NAME);
        dummyWeather.setCurrentTemperature("");
        dummyWeather.setHumidity(1);
        dummyWeather.setIcon(null);
        dummyWeather.setIconId("");
        dummyWeather.setWeatherDescription("");
        return dummyWeather;
    }
}
