package com.example.julia.weatherguide.repositories.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.julia.weatherguide.repositories.CurrentWeatherDataModel;
import com.example.julia.weatherguide.WeatherGuideApplication;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by julia on 15.07.17.
 */

public class SharedPreferenceService {

    private static final String CURRENT_WEATHER_PREFERENCES = "current_weather";
    private static final String PREF_TEMPERATURE = "current_temperature";
    private static final String PREF_DESCRIPTION = "current_description";
    private static final String PREF_ICON_ID = "icon_id";
    private static final String PREF_HUMIDITY = "humidity";
    private static final String PREF_LOCATION_ID = "location_id";
    private static final String DEF_LOCATION_ID = "524901";
    private static final String PREF_LOCATION_NAME = "location_name";
    private static final String DEF_LOCATION_NAME = "Moscow";
    private static final String DEF_TEMPERATURE = null ;
    private static final String DEF_DESCRIPTION = null;
    private static final String DEF_ICON_ID = null;
    private static final int DEF_HUMIDITY = 0;

    private static SharedPreferenceService instance = null;

    public synchronized static SharedPreferenceService getService() {
        if (instance == null) {
            instance = new SharedPreferenceService();
        }
        return instance;
    }

    public boolean hasValueInStorage() {
        Context context = WeatherGuideApplication.getInstance().getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(CURRENT_WEATHER_PREFERENCES, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(sharedPreferences.getString(PREF_DESCRIPTION, DEF_DESCRIPTION))) {
            return false;
        }
        return true;
    }
    //TODO: has dummy argument for unify interface
    public Observable<CurrentWeatherDataModel> getCurrentWeather (@NonNull String location) {
       return Observable.create(new ObservableOnSubscribe<CurrentWeatherDataModel>() {
           @Override
           public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<CurrentWeatherDataModel> e) throws Exception {
               if (hasValueInStorage()) {
                   e.onNext(readFromSharedPreferences());
               }
               e.onComplete();
           }
       });
    }

    public void saveToSharedPreferences(@NonNull CurrentWeatherDataModel data) {

        Context context = WeatherGuideApplication.getInstance().getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(CURRENT_WEATHER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LOCATION_ID, data.getLocationId());
        editor.putString(PREF_LOCATION_NAME, data.getLocationName());
        editor.putString(PREF_DESCRIPTION, data.getWeatherDescription());
        editor.putInt(PREF_HUMIDITY, data.getHumidity());
        editor.putString(PREF_ICON_ID, data.getIconId());
        editor.putString(PREF_TEMPERATURE, data.getCurrentTemperature());
        editor.commit();
    }

    private CurrentWeatherDataModel readFromSharedPreferences() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        CurrentWeatherDataModel data = new CurrentWeatherDataModel();
        Context context = WeatherGuideApplication.getInstance().getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(CURRENT_WEATHER_PREFERENCES, Context.MODE_PRIVATE);
        data.setLocationId(sharedPreferences.getString(PREF_LOCATION_ID, DEF_LOCATION_ID));
        data.setLocationName(sharedPreferences.getString(PREF_LOCATION_NAME, DEF_LOCATION_NAME));
        data.setCurrentTemperature(sharedPreferences.getString(PREF_TEMPERATURE, DEF_TEMPERATURE));
        data.setWeatherDescription(sharedPreferences.getString(PREF_DESCRIPTION, DEF_DESCRIPTION));
        data.setHumidity(sharedPreferences.getInt(PREF_HUMIDITY, DEF_HUMIDITY));
        data.setIconId(sharedPreferences.getString(PREF_ICON_ID, DEF_ICON_ID));
        return data;
    }

    public String getCurrentLocationId() {
        Context context = WeatherGuideApplication.getInstance().getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(CURRENT_WEATHER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_LOCATION_ID, DEF_LOCATION_ID);
    }
}
