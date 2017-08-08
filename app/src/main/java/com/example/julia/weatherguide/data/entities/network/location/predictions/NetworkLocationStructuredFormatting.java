package com.example.julia.weatherguide.data.entities.network.location.predictions;


import com.example.julia.weatherguide.utils.Preconditions;
import com.google.gson.annotations.SerializedName;

public class NetworkLocationStructuredFormatting {

    @SerializedName("main_text")
    private String mainText;

    @SerializedName("secondary_text")
    private String secondaryText;

    public NetworkLocationStructuredFormatting(String mainText, String secondaryText) {
        Preconditions.nonNull(mainText, secondaryText);
        this.mainText = mainText;
        this.secondaryText = secondaryText;
    }
    public String getMainText() {
        return mainText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

}
