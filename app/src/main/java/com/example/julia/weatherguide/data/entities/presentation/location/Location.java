package com.example.julia.weatherguide.data.entities.presentation.location;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.julia.weatherguide.utils.Preconditions;

public class Location implements Parcelable {

    public final double longitude;

    public final double latitude;

    public final String name;

    public final boolean isCurrent;

    public Location(double longitude, double latitude, String name) {
        this(longitude, latitude, name, false);
    }

    public Location(double longitude, double latitude, String name, boolean isCurrent) {
        Preconditions.nonNull(name);

        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.isCurrent = isCurrent;
    }

    public boolean hasTheSameData(Location location) {
        return coordinatesMatches(location)
            && location.name.equals(this.name);
    }

    public boolean coordinatesMatches(Location location) {
        return location != null
            && location.latitude == this.latitude
            && location.longitude == this.longitude
            && location.isCurrent == this.isCurrent;
    }

    // -------------------------------------- Parcellable -----------------------------------------

    public Location(Parcel parcel) {
        this.longitude = parcel.readDouble();
        this.latitude = parcel.readDouble();
        this.name = parcel.readString();
        this.isCurrent = parcel.readInt() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(name);
        dest.writeInt(isCurrent ? 1 : 0);
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
