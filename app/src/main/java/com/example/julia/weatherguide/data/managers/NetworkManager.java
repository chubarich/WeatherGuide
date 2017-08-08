package com.example.julia.weatherguide.data.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkManager {

    private final Context applicationContext;

    public NetworkManager(Context context) {
        applicationContext = context.getApplicationContext();
    }

    public NetworkStatus getStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return NetworkStatus.UNKNOWN;
        }

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI
                || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NetworkStatus.CONNECTED;
            } else {
                return NetworkStatus.DISCONNECTED;
            }
        } else {
            return NetworkStatus.DISCONNECTED;
        }
    }

    public enum NetworkStatus {
        CONNECTED,
        DISCONNECTED,
        UNKNOWN;
    }
}
