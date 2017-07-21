package com.example.julia.weatherguide.ui.current_weather;

import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;

/**
 * Created by julia on 16.07.17.
 */

public class CurrentWeatherViewState {

    private boolean isLoading;

    private boolean hasData;

    private String location;

    private boolean reasonForLoadingUpdate = false;

    private CurrentWeatherDataModel data;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public boolean isReasonForLoadingUpdate() {
        return reasonForLoadingUpdate;
    }

    public void setReasonForLoadingUpdate(boolean reasonForLoadingUpdate) {
        this.reasonForLoadingUpdate = reasonForLoadingUpdate;
    }

    public CurrentWeatherDataModel getData() {
        return data;
    }

    public void setData(CurrentWeatherDataModel data) {
        this.hasData = true;
        this.data = data;
    }

    public boolean hasData() {
        return hasData;
    }
}
