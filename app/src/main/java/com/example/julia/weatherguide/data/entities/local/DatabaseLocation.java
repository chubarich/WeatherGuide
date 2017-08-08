package com.example.julia.weatherguide.data.entities.local;


import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import static com.example.julia.weatherguide.data.contracts.local.location.LocationContract.*;

@StorIOSQLiteType(table = TABLE_NAME)
public class DatabaseLocation {

    @StorIOSQLiteColumn(name = COLUMN_NAME_ID, key = true)
    Long _id;

    @StorIOSQLiteColumn(name = COLUMN_NAME_LONGITUDE)
    float longitude;

    @StorIOSQLiteColumn(name = COLUMN_NAME_LATITUDE)
    float latitude;

    @StorIOSQLiteColumn(name = COLUMN_NAME_NAME)
    String name;

    DatabaseLocation() {
    }

    public DatabaseLocation(float longitude, float latitude, String name) {
        this._id = null;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public Long getId() {
        return _id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

}
