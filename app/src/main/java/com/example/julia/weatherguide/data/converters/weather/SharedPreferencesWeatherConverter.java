package com.example.julia.weatherguide.data.converters.weather;

import android.content.res.Resources;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.data.data_services.settings.SettingsService;
import com.example.julia.weatherguide.data.entities.local.DatabaseCurrentWeather;
import com.example.julia.weatherguide.data.entities.local.DatabaseWeatherPrediction;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkCurrentWeather;
import com.example.julia.weatherguide.data.entities.network.weather.NetworkWeatherPrediction;
import com.example.julia.weatherguide.data.entities.presentation.weather.CurrentWeather;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.data.entities.presentation.weather.WeatherPrediction;
import com.example.julia.weatherguide.data.helpers.DatetimeHelper;
import com.example.julia.weatherguide.utils.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.julia.weatherguide.data.converters.weather.SharedPreferencesWeatherConverter.TemperatureType.CELSIUS;
import static com.example.julia.weatherguide.data.converters.weather.SharedPreferencesWeatherConverter.TemperatureType.FAHRENHEIT;
import static com.example.julia.weatherguide.data.helpers.DatetimeHelperPlain.DayOfMonth;


public class SharedPreferencesWeatherConverter implements WeatherConverter {

    private static final String CONDITION_DESCRIPTION_NAME_PREFIX = "id_";
    private static final String CONDITION_ICON_NAME_PREFIX = "w";
    private static final double DIFFERENCE_KELVIN_CELSIUS = 273.5;
    private static final double DIFFERENCE_FAHRENHEIT_CELSIUS = 32.0;
    private static final double COEFFICIENT_FAHRENHEIT_CELSIUS = 9.0 / 5.0;
    private static final double COEFFICIENT_PRESSURE = 0.75;
    private static final double COEFFICIENT_WIND = 3.6;

    private final DatetimeHelper datetimeHelper;
    private final Resources resources;
    private final SettingsService settingsService;
    private final String applicationPackageName;

    public SharedPreferencesWeatherConverter(DatetimeHelper datetimeHelper, Resources resources,
                                             SettingsService settingsService,
                                             String applicationPackageName) {
        Preconditions.nonNull(datetimeHelper, resources, settingsService, applicationPackageName);
        this.datetimeHelper = datetimeHelper;
        this.resources = resources;
        this.settingsService = settingsService;
        this.applicationPackageName = applicationPackageName;
    }

    @Override
    public Weather fromNetwork(NetworkCurrentWeather networkCurrentWeather,
                               List<NetworkWeatherPrediction> networkPredictions) {
        CurrentWeather currentWeather = fromNetworkWeather(networkCurrentWeather);

        List<String> dates = datetimeHelper.getNextDates(networkPredictions.size());
        Collections.sort(networkPredictions, (o1, o2) -> o1.getTimestamp() > o2.getTimestamp() ? 1 : -1);
        List<WeatherPrediction> predictions = new ArrayList<>();
        for (int i = 0; i < dates.size(); ++i) {
            NetworkWeatherPrediction networkPrediction = networkPredictions.get(i);
            predictions.add(fromNetworkPrediction(networkPrediction, dates.get(i)));
        }

        return new Weather(currentWeather, predictions);
    }

    @Override
    public Weather fromDatabase(DatabaseCurrentWeather databaseCurrentWeather,
                                List<DatabaseWeatherPrediction> databasePredictions) {
        CurrentWeather currentWeather = fromDatabaseWeather(databaseCurrentWeather);

        Collections.sort(databasePredictions, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        List<WeatherPrediction> predictions = new ArrayList<>();
        for (DatabaseWeatherPrediction databasePrediction : databasePredictions) {
            predictions.add(fromDatabasePrediction(databasePrediction));
        }

        return new Weather(currentWeather, predictions);
    }

    @Override
    public DatabaseCurrentWeather fromNetwork(NetworkCurrentWeather weather, long locationId) {
        return new DatabaseCurrentWeather.Builder()
            .pressure(weather.getPressure())
            .humidity((int)weather.getHumidity())
            .cloudiness((int)weather.getCloudiness())
            .conditionIconName(weather.getConditionIconName())
            .mainTemperature(weather.getTemperature())
            .windAngle(weather.getWindAngle())
            .windSpeed(weather.getWindSpeed())
            .locationId(locationId)
            .conditionId(weather.getConditionId())
            .timestampOfUpdate(datetimeHelper.getCurrentTimestampGMT())
            .build();
    }

    @Override
    public List<DatabaseWeatherPrediction> fromNetwork(List<NetworkWeatherPrediction> networkPredictions,
                                                       long locationId) {
        Collections.sort(networkPredictions, (o1, o2) -> o1.getTimestamp() > o2.getTimestamp() ? 1 : -1);
        List<String> dates = datetimeHelper.getNextDates(networkPredictions.size());

        List<DatabaseWeatherPrediction> predictions = new ArrayList<>();
        for (int i = 0; i < networkPredictions.size(); ++i) {
            NetworkWeatherPrediction networkPrediction = networkPredictions.get(i);
            predictions.add(fromNetworkPredictionToDatabase(networkPrediction, locationId, dates.get(i)));
        }
        return predictions;
    }

    @Override
    public Integer getTemperature(DatabaseCurrentWeather weather) {
        TemperatureType temperatureType = settingsService.isTemperatureTypeInFahrenheit()
            ? FAHRENHEIT : CELSIUS;

        return convertKalvin(temperatureType, weather.getTemperature());
    }

    // --------------------------------------- private --------------------------------------------

    private CurrentWeather fromNetworkWeather(NetworkCurrentWeather weather) {
        boolean isHpa = settingsService.isPressureInHpa();
        TemperatureType temperatureType = settingsService.isTemperatureTypeInFahrenheit()
            ? FAHRENHEIT : CELSIUS;
        boolean isKph = settingsService.isWeatherSpeedInKph();

        return new CurrentWeather.Builder()
            .datetimeOfUpdate(datetimeHelper.getTimeForUpdate(datetimeHelper.getCurrentTimestampGMT()))
            .conditionDescription(getConditionDescription(weather.getConditionId()))
            .conditionIconId(getConditionIconId(weather.getConditionIconName()))
            .cloudiness((int) weather.getCloudiness())
            .humidity((int) weather.getHumidity())
            .mainTemperature(convertKalvin(temperatureType, weather.getTemperature()))
            .pressureSummary(getPressureSummary(weather.getPressure(), isHpa))
            .windSummary(getWindSummary(weather.getWindSpeed(), isKph))
            .build();
    }

    private CurrentWeather fromDatabaseWeather(DatabaseCurrentWeather weather) {
        boolean isHpa = settingsService.isPressureInHpa();
        TemperatureType temperatureType = settingsService.isTemperatureTypeInFahrenheit()
            ? FAHRENHEIT : CELSIUS;
        boolean isKph = settingsService.isWeatherSpeedInKph();

        return new CurrentWeather.Builder()
            .datetimeOfUpdate(datetimeHelper.getTimeForUpdate(weather.getTimestampOfUpdate()))
            .conditionDescription(getConditionDescription(weather.getConditionId()))
            .conditionIconId(getConditionIconId(weather.getConditionIconName()))
            .cloudiness(weather.getCloudiness())
            .humidity(weather.getHumidity())
            .mainTemperature(convertKalvin(temperatureType, weather.getTemperature()))
            .pressureSummary(getPressureSummary(weather.getPressure(), isHpa))
            .windSummary(getWindSummary(weather.getWindSpeed(), isKph))
            .build();
    }

    private WeatherPrediction fromNetworkPrediction(NetworkWeatherPrediction prediction, String date) {
        boolean isHpa = settingsService.isPressureInHpa();
        TemperatureType temperatureType = settingsService.isTemperatureTypeInFahrenheit()
            ? FAHRENHEIT : CELSIUS;
        boolean isKph = settingsService.isWeatherSpeedInKph();

        return new WeatherPrediction.Builder()
            .date(getDate(datetimeHelper.getTimeForPrediction(date)))
            .conditionDescription(getConditionDescription(prediction.getConditionId()))
            .conditionIconId(getConditionIconId(prediction.getConditionIconId()))
            .windSummary(getWindSummary(prediction.getWindSpeed(), isKph))
            .pressure(getPressureSummary(prediction.getPressure(), isHpa))
            .maxTemperature(convertKalvin(temperatureType, prediction.getMaxTemperature()))
            .minTemperature(convertKalvin(temperatureType, prediction.getMinTemperature()))
            .morningTemperature(convertKalvin(temperatureType, prediction.getMorningTemperature()))
            .dayTemperature(convertKalvin(temperatureType, prediction.getDayTemperature()))
            .eveningTemperature(convertKalvin(temperatureType, prediction.getEveningTemperature()))
            .nightTemperature(convertKalvin(temperatureType, prediction.getMaxTemperature()))
            .humidity((int) prediction.getHumidity())
            .cloudiness((int)prediction.getCloudiness())
            .build();
    }

    private DatabaseWeatherPrediction fromNetworkPredictionToDatabase(NetworkWeatherPrediction prediction,
                                                                      long locationId,
                                                                      String date) {
        return new DatabaseWeatherPrediction.Builder()
            .conditionId(prediction.getConditionId())
            .locationId(locationId)
            .windAngle(prediction.getWindAngle())
            .windSpeed(prediction.getWindSpeed())
            .cloudiness((int)prediction.getCloudiness())
            .conditionIconId(prediction.getConditionIconId())
            .date(date)
            .dayTemperature(prediction.getDayTemperature())
            .eveningTemperature(prediction.getEveningTemperature())
            .conditionId(prediction.getConditionId())
            .humidity((int)prediction.getHumidity())
            .pressure(prediction.getPressure())
            .maxTemperature(prediction.getMaxTemperature())
            .minTemperature(prediction.getMinTemperature())
            .morningTemperature(prediction.getMorningTemperature())
            .nightTemperature(prediction.getNightTemperature())
            .build();
    }

    private WeatherPrediction fromDatabasePrediction(DatabaseWeatherPrediction prediction) {
        boolean isHpa = settingsService.isPressureInHpa();
        TemperatureType temperatureType = settingsService.isTemperatureTypeInFahrenheit()
            ? FAHRENHEIT : CELSIUS;
        boolean isKph = settingsService.isWeatherSpeedInKph();

        return new WeatherPrediction.Builder()
            .date(getDate(datetimeHelper.getTimeForPrediction(prediction.getDate())))
            .conditionDescription(getConditionDescription(prediction.getConditionId()))
            .conditionIconId(getConditionIconId(prediction.getConditionIconId()))
            .windSummary(getWindSummary(prediction.getWindSpeed(), isKph))
            .pressure(getPressureSummary(prediction.getPressure(), isHpa))
            .maxTemperature(convertKalvin(temperatureType, prediction.getMaxTemperature()))
            .minTemperature(convertKalvin(temperatureType, prediction.getMinTemperature()))
            .morningTemperature(convertKalvin(temperatureType, prediction.getMorningTemperature()))
            .dayTemperature(convertKalvin(temperatureType, prediction.getDayTemperature()))
            .eveningTemperature(convertKalvin(temperatureType, prediction.getEveningTemperature()))
            .nightTemperature(convertKalvin(temperatureType, prediction.getMaxTemperature()))
            .humidity(prediction.getHumidity())
            .cloudiness(prediction.getCloudiness())
            .build();
    }

    private String getConditionDescription(int weatherConditionId) {
        String descriptionName = CONDITION_DESCRIPTION_NAME_PREFIX + weatherConditionId;
        int resId = resources.getIdentifier(descriptionName, "string", applicationPackageName);
        return resources.getString(resId);
    }

    private int getConditionIconId(String iconName) {
        String fullIconName = CONDITION_ICON_NAME_PREFIX + iconName;
        return resources.getIdentifier(fullIconName, "drawable", applicationPackageName);
    }

    private String getDate(DayOfMonth dayOfMonth) {
        if (dayOfMonth.isToday) {
            return resources.getString(R.string.today);
        } else if (dayOfMonth.isTomorrow) {
            return resources.getString(R.string.tomorrow);
        } else {
            return dayOfMonth.day + " " + resources.getStringArray(R.array.months)[dayOfMonth.month];
        }
    }

    private String getWindSummary(double speed, boolean isKph) {
        return isKph ? convertWindSpeedToKph(speed) + " " + resources.getString(R.string.kph)
            : speed + " " + resources.getString(R.string.mps);
    }

    private String getPressureSummary(double pressure, boolean isHpa) {
        return isHpa ? (int) pressure + " " + resources.getString(R.string.hPa)
            : convertPressureToMMHG(pressure) + " " + resources.getString(R.string.mmHg);
    }

    private int convertKalvin(TemperatureType temperatureType, double temperature) {
        double celsius = temperature - DIFFERENCE_KELVIN_CELSIUS;
        if (temperatureType == CELSIUS) {
            return (int) Math.round(celsius);
        } else {
            return (int) Math.round(celsius * COEFFICIENT_FAHRENHEIT_CELSIUS + DIFFERENCE_FAHRENHEIT_CELSIUS);
        }
    }

    private static double convertWindSpeedToKph(double speed) {
        return speed * COEFFICIENT_WIND;
    }

    private static int convertPressureToMMHG(double pressure) {
        return (int) (pressure * COEFFICIENT_PRESSURE);
    }

    public enum TemperatureType {
        CELSIUS,
        FAHRENHEIT
    }

}
