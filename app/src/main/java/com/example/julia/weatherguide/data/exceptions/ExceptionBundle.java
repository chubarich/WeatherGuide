package com.example.julia.weatherguide.data.exceptions;

import android.os.Bundle;

public class ExceptionBundle extends Exception {

    private ExceptionBundle.Reason reason;
    private Bundle extras;


    public ExceptionBundle(Reason reason) {
        this.reason = reason;
        this.extras = new Bundle();
    }

    public Reason getReason() {
        return reason;
    }

    public void addStringExtra(String key, String value) {
        this.extras.putString(key, value);
    }

    public void addIntExtra(String key, int extra) {
        this.extras.putInt(key, extra);
    }

    public String getStringExtra(String key) {
        return this.extras.getString(key);
    }

    public int getIntExtra(String key) {
        return this.extras.getInt(key);
    }


    public enum Reason {

        // general exceptions
        UNKNOWN(-1),
        NETWORK_UNAVAILABLE(0),
        LOCATION_NOT_INITIALIZED(100),

        // api exceptions
        API_ERROR(200),

        // database
        EMPTY_DATABASE(300),
        NOT_DELETED(301);

        private final int code;

        Reason(int code) {
            this.code = code;
        }
    }
}