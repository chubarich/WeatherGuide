package com.example.julia.weatherguide.data.entities.presentation.location;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.julia.weatherguide.utils.Preconditions;

public class Location implements Parcelable {

    public final float longitude;

    public final float latitude;

    public final String name;

    public Location(final float longitude, final float latitude, final String name) {
        Preconditions.nonNull(name);

        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public boolean coordinatesMatches(Location location) {
        return location != null && location.latitude == this.latitude
            && location.longitude == this.longitude;
    }

    // -------------------------------------- Parcellable -----------------------------------------

    public Location(Parcel parcel) {
        this.longitude = parcel.readFloat();
        this.latitude = parcel.readFloat();
        this.name = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
