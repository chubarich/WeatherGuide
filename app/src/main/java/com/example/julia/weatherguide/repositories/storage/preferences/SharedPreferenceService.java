package com.example.julia.weatherguide.repositories.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

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
  private static final String DEF_TEMPERATURE = null;
  private static final String DEF_DESCRIPTION = null;
  private static final String DEF_ICON_ID = null;
  private static final int DEF_HUMIDITY = 0;

  private final Context context;

  public SharedPreferenceService(Context context) {
    this.context = context;
  }

  public Maybe<CurrentWeatherDataModel> getCurrentWeather(@NonNull String location) {
    return Maybe.create(e -> {
      if (hasValueInStorage()) {
        e.onSuccess(readFromSharedPreferences());
      }
    });
  }

  public void saveToSharedPreferences(@NonNull CurrentWeatherDataModel data) {
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
    CurrentWeatherDataModel data = new CurrentWeatherDataModel();
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
    SharedPreferences sharedPreferences = context.getSharedPreferences(CURRENT_WEATHER_PREFERENCES, Context.MODE_PRIVATE);
    return sharedPreferences.getString(PREF_LOCATION_ID, DEF_LOCATION_ID);
  }

  private boolean hasValueInStorage() {
    SharedPreferences sharedPreferences = context.getSharedPreferences(CURRENT_WEATHER_PREFERENCES, Context.MODE_PRIVATE);
    return (!TextUtils.isEmpty(sharedPreferences.getString(PREF_DESCRIPTION, DEF_DESCRIPTION)));
  }
}
