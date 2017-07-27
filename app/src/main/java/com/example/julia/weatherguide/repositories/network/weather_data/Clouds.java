package com.example.julia.weatherguide.repositories.network.weather_data;

import com.google.gson.annotations.SerializedName;

class Clouds {

    @SerializedName("all")
    private Integer all;

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

}
