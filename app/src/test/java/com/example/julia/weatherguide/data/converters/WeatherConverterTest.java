package com.example.julia.weatherguide.data.converters;

import android.content.res.Resources;

import com.example.julia.weatherguide.data.converters.weather.SharedPreferencesWeatherConverter;
import com.example.julia.weatherguide.data.converters.weather.WeatherConverter;
import com.example.julia.weatherguide.data.data_services.settings.SettingsService;
import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeather;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPrediction;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkClouds;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkCondition;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkCurrentWeather;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkMain;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkTemperatures;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkWeatherPrediction;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkWind;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.data.helpers.DatetimeHelper;
import com.example.julia.weatherguide.data.helpers.DatetimeHelperPlain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class WeatherConverterTest {

    private static final String STRING = "DEFAULT";
    private final Random random = new Random(124);

    private WeatherConverter converter;
    private Resources resources;
    private SettingsService settingsService;
    private DatetimeHelper datetimeHelper;

    @Before
    public void before() {
        datetimeHelper = new DatetimeHelperPlain();
        resources = mock(Resources.class);
        settingsService = mock(SettingsService.class);
        converter = new SharedPreferencesWeatherConverter(datetimeHelper, resources, settingsService, "");
    }

    @Test
    public void databaseCurrentWeather_fromNetwork_isConsistent() {
        NetworkCurrentWeather weather = getDummyNetworkCurrentWeather();
        long id = rInt();

        DatabaseCurrentWeather databaseWeather = converter.fromNetwork(weather, id);

        assertTrue(databaseWeather.getConditionIconName().equals(weather.getConditionIconName()));
        assertEquals(databaseWeather.getConditionId(), weather.getConditionId());
        assertEquals(databaseWeather.getCloudiness(), (int)weather.getCloudiness());
        assertEquals(databaseWeather.getHumidity(), (int)weather.getHumidity());
        assertEquals(databaseWeather.getLocationId(), id);
        assertEquals(databaseWeather.getWindAngle(), weather.getWindAngle());
        assertEquals(databaseWeather.getTemperature(), weather.getTemperature());
        assertEquals(databaseWeather.getPressure(), weather.getPressure());
        assertEquals(databaseWeather.getWindSpeed(), weather.getWindSpeed());
    }

    @Test
    public void databaseWeatherPredictions_fromNetwork_isConsistent() {
        long id = rInt();

        NetworkWeatherPrediction network = getDummyNetworkWeatherPrediction();
        List<NetworkWeatherPrediction> predictions = new ArrayList<>();
        predictions.add(network);

        List<DatabaseWeatherPrediction> result = converter.fromNetwork(predictions, id);


        assertTrue(result.size() == 1);
        DatabaseWeatherPrediction database = result.get(0);

        assertEquals(database.getLocationId(), id);
        assertTrue(database.getDate().equals(datetimeHelper.getDateFromTimestamp(network.getTimestamp())));
        assertEquals(database.getConditionId(), network.getConditionId());
        assertEquals(database.getConditionIconId(), network.getConditionIconId());
        assertEquals(database.getMinTemperature(), network.getMinTemperature());
        assertEquals(database.getMaxTemperature(), network.getMaxTemperature());
        assertEquals(database.getMorningTemperature(), network.getMorningTemperature());
        assertEquals(database.getDayTemperature(), network.getDayTemperature());
        assertEquals(database.getEveningTemperature(), network.getEveningTemperature());
        assertEquals(database.getNightTemperature(), network.getNightTemperature());
        assertEquals(database.getWindSpeed() , network.getWindSpeed());
        assertEquals(database.getWindAngle(), network.getWindAngle());
        assertEquals(database.getHumidity(), (int)network.getHumidity());
        assertEquals(database.getPressure(), network.getPressure());
        assertEquals(database.getCloudiness(), (int)network.getCloudiness());
    }



    private NetworkCurrentWeather getDummyNetworkCurrentWeather() {
        NetworkClouds networkClouds = new NetworkClouds(rDouble());
        NetworkWind networkWind = new NetworkWind(rDouble(), rDouble());
        NetworkMain networkMain = new NetworkMain(rDouble(), rDouble(), rDouble());
        NetworkCondition networkCondition = new NetworkCondition(rInt(), STRING);
        return new NetworkCurrentWeather(networkCondition, networkMain, networkWind, networkClouds);
    }

    private NetworkWeatherPrediction getDummyNetworkWeatherPrediction() {
        NetworkTemperatures networkTemperatures = new NetworkTemperatures(rInt(), rInt(), rInt(),
                rInt(), rInt(), rInt());
        NetworkCondition networkCondition = new NetworkCondition(rInt(), STRING);
        List<NetworkCondition> conditions = new ArrayList<>();
        conditions.add(networkCondition);

        return new NetworkWeatherPrediction(datetimeHelper.getCurrentTimestampGMT(),
                networkTemperatures, rDouble(), rDouble(), conditions, rDouble(), rDouble(), rInt());
    }

    private double rDouble() {
        return random.nextDouble();
    }

    private int rInt() {
        return random.nextInt();
    }


}
