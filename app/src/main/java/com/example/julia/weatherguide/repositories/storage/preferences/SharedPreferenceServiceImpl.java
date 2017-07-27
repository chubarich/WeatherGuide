package com.example.julia.weatherguide.repositories.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.julia.weatherguide.repositories.data.Location;
import com.example.julia.weatherguide.repositories.data.WeatherDataModel;
import com.example.julia.weatherguide.repositories.exception.ExceptionBundle;

import java.util.IllegalFormatCodePointException;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class SharedPreferenceServiceImpl implements SharedPreferenceService {

    private static final String PREF_TEMPERATURE = "current_temperature";
    private static final String PREF_DESCRIPTION = "current_description";
    private static final String PREF_ICON_ID = "icon_id";
    private static final String PREF_HUMIDITY = "humidity";
    private static final String PREF_LOCATION_NAME = "location_name";
    private static final String PREF_LATITUDE = "latitude";
    private static final String PREF_LONGITUDE = "longitude";

    private static final String DEF_LOCATION_NAME = "";
    private static final String DEF_TEMPERATURE = "";
    private static final String DEF_DESCRIPTION = "";
    private static final String DEF_ICON_ID = "";
    private static final float DEF_LATITUDE = 0;
    private static final float DEF_LONGITUDE = 0;
    private static final int DEF_HUMIDITY = 0;

    private final SharedPreferences sharedPreferences;

    public SharedPreferenceServiceImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    // ----------------------------------------- public ---------------------------------------------

    @Override
    public Single<WeatherDataModel> getCurrentWeather() {
        if (!isLocationInitialized()) {
            return Single.error(new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED));
        } else {
            if (hasValueInStorage()) {
                return Single.fromCallable(this::readFromSharedPreferences);
            } else {
                return Single.error(new ExceptionBundle(ExceptionBundle.Reason.EMPTY_DATABASE));
            }
        }
    }

    @Override
    public Completable saveCurrentLocation(Location location, String locationName) {
        return Completable.fromAction(
            () -> sharedPreferences.edit()
                .remove(PREF_TEMPERATURE)
                .remove(PREF_DESCRIPTION)
                .remove(PREF_HUMIDITY)
                .remove(PREF_ICON_ID)
                .putFloat(PREF_LATITUDE, location.latitude)
                .putFloat(PREF_LONGITUDE, location.longitude)
                .putString(PREF_LOCATION_NAME, locationName)
                .commit()
        );
    }

    @Override
    public Completable saveWeatherForCurrentLocation(@NonNull WeatherDataModel data) {
        if (!isLocationInitialized()) {
            return Completable.error(new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED));
        } else {
            return Completable.fromAction(
                () -> sharedPreferences.edit()
                    .putString(PREF_DESCRIPTION, data.getWeatherDescription())
                    .putInt(PREF_HUMIDITY, data.getHumidity())
                    .putString(PREF_ICON_ID, data.getIconId())
                    .putString(PREF_TEMPERATURE, data.getCurrentTemperature())
                    .commit()
            );
        }
    }

    @Override
    public Location getCurrentLocation() {
        if (isLocationInitialized()) {
            float latitude = sharedPreferences.getFloat(PREF_LATITUDE, DEF_LATITUDE);
            float longitude = sharedPreferences.getFloat(PREF_LONGITUDE, DEF_LONGITUDE);
            return new Location(longitude, latitude);
        } else {
            return null;
        }
    }

    @Override
    public String getCurrentLocationName() {
        if (isLocationInitialized()) {
            return sharedPreferences.getString(PREF_LOCATION_NAME, "");
        } else {
            return null;
        }
    }

    @Override
    public boolean isLocationInitialized() {
        return sharedPreferences.contains(PREF_LONGITUDE)
            && sharedPreferences.contains(PREF_LATITUDE)
            && sharedPreferences.contains(PREF_LOCATION_NAME);
    }

    // ----------------------------------------- private --------------------------------------------

    private WeatherDataModel readFromSharedPreferences() throws ExceptionBundle {
        if (!isLocationInitialized())
            throw new ExceptionBundle(ExceptionBundle.Reason.LOCATION_NOT_INITIALIZED);

        WeatherDataModel data = new WeatherDataModel();
        data.setLocationName(sharedPreferences.getString(PREF_LOCATION_NAME, DEF_LOCATION_NAME));
        data.setCurrentTemperature(sharedPreferences.getString(PREF_TEMPERATURE, DEF_TEMPERATURE));
        data.setWeatherDescription(sharedPreferences.getString(PREF_DESCRIPTION, DEF_DESCRIPTION));
        data.setHumidity(sharedPreferences.getInt(PREF_HUMIDITY, DEF_HUMIDITY));
        data.setIconId(sharedPreferences.getString(PREF_ICON_ID, DEF_ICON_ID));

        return data;
    }

    private boolean hasValueInStorage() {
        return sharedPreferences.contains(PREF_DESCRIPTION);
    }

}
