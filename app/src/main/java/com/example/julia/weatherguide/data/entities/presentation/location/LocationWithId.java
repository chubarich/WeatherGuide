package com.example.julia.weatherguide.data.entities.presentation.location;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.utils.Preconditions;


public class LocationWithId implements Parcelable {

    public final long id;

    public final Location location;

    public LocationWithId(long id, Location location) {
        Preconditions.nonNull(location);
        this.id = id;
        this.location = location;
    }

    // -------------------------------------- Parcellable -----------------------------------------

    public LocationWithId(Parcel parcel) {
        this.id = parcel.readLong();
        this.location = parcel.readParcelable(Location.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(location, flags);
    }

    public static final Parcelable.Creator<LocationWithId> CREATOR = new Parcelable.Creator<LocationWithId>() {
        @Override
        public LocationWithId createFromParcel(Parcel source) {
            return new LocationWithId(source);
        }

        @Override
        public LocationWithId[] newArray(int size) {
            return new LocationWithId[size];
        }
    };

}
