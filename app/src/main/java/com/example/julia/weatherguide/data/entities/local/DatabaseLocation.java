package com.example.julia.weatherguide.data.entities.local;


import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import static com.example.julia.weatherguide.data.contracts.local.location.LocationContract.*;

@StorIOSQLiteType(table = TABLE_NAME)
public class DatabaseLocation {

    @StorIOSQLiteColumn(name = COLUMN_NAME_ID, key = true)
    Long _id;

    @StorIOSQLiteColumn(name = COLUMN_NAME_LONGITUDE)
    double longitude;

    @StorIOSQLiteColumn(name = COLUMN_NAME_LATITUDE)
    double latitude;

    @StorIOSQLiteColumn(name = COLUMN_NAME_NAME)
    String name;

    DatabaseLocation() {
    }

    public DatabaseLocation(double longitude, double latitude, String name) {
        this(null, longitude, latitude, name);
    }

    public DatabaseLocation(Long _id, double longitude, double latitude, String name) {
        this._id = _id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public Long getId() {
        return _id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

}
