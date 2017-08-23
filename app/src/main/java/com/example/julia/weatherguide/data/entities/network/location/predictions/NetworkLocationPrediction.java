package com.example.julia.weatherguide.data.entities.network.location.predictions;


import com.example.julia.weatherguide.utils.Preconditions;
import com.google.gson.annotations.SerializedName;

public class NetworkLocationPrediction {

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("structured_formatting")
    private NetworkLocationStructuredFormatting structuredFormatting;

    public NetworkLocationPrediction(String placeId, NetworkLocationStructuredFormatting structuredFormatting) {
        Preconditions.nonNull(placeId, structuredFormatting);
        this.placeId = placeId;
        this.structuredFormatting = structuredFormatting;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getMainText() {
        return structuredFormatting.getMainText();
    }

    public String getSecondaryText() {
        return structuredFormatting.getSecondaryText();
    }

}
