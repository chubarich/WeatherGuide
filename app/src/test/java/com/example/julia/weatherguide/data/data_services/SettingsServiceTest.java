package com.example.julia.weatherguide.data.data_services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.julia.weatherguide.data.data_services.settings.SettingsService;
import com.example.julia.weatherguide.data.data_services.settings.SharedPreferenceService;
import com.example.julia.weatherguide.utils.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
public class SettingsServiceTest {

    private static final String SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME";

    private SettingsService settingsService;

    @Before
    public void before() {
        SharedPreferences sharedPreferences = RuntimeEnvironment.application
                .getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        this.settingsService = new SharedPreferenceService(sharedPreferences);
    }

    @Test
    public void isTemperatureTypeInFahrenheit_defaultFalse() {
        boolean isFahrenheit = settingsService.isTemperatureTypeInFahrenheit();

        assertFalse(isFahrenheit);
    }

    @Test
    public void isWeatherSpeedInKph_defaultFalse() {
        boolean isKph = settingsService.isWeatherSpeedInKph();

        assertFalse(isKph);
    }

    @Test
    public void isPressureInHpa_defaultFalse() {
        boolean isHpa = settingsService.isPressureInHpa();

        assertFalse(isHpa);
    }

    @Test
    public void getCurrentLocationId_defaultNull() {
        Long currentLocationId = settingsService.getCurrentLocationId();

        assertNull(currentLocationId);
    }

    @Test
    public void getCurrentLocationId_returnsLatest() {
        settingsService.setCurrentLocationId(5L);
        settingsService.setCurrentLocationId(15L);
        assertEquals(15L, (long)settingsService.getCurrentLocationId());

        settingsService.setCurrentLocationId(35L);
        settingsService.setCurrentLocationId(1L);
        assertEquals(1L, (long)settingsService.getCurrentLocationId());
    }

    @Test
    public void subscribeOnCurrentLocationIdChanges_observesResults() {
        TestObserver<Optional<Long>> testObserver = settingsService
                .subscribeOnCurrentLocationIdChanges()
                .test();

        testObserver.assertNoErrors().assertValueAt(0, optional -> !optional.isPresent());

        settingsService.setCurrentLocationId(1L);
        testObserver.assertNoErrors().assertValueAt(1, optional -> optional.get() == 1L);

        settingsService.setCurrentLocationId(10L);
        testObserver.assertNoErrors().assertValueAt(2, optional -> (long)optional.get() == 10L);
    }

}
