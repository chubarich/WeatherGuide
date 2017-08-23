package com.example.julia.weatherguide.data.entities.network.weather;

import com.example.julia.weatherguide.utils.Preconditions;
import com.google.gson.annotations.SerializedName;


public class NetworkCondition {

    @SerializedName("id")
    private int id;

    @SerializedName("icon")
    private String iconId;

    public NetworkCondition(int id, String iconId) {
        Preconditions.nonNull(iconId);
        this.id = id;
        this.iconId = iconId;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String icon) {
        this.iconId = icon;
    }

}
