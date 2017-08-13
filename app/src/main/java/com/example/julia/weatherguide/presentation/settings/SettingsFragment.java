package com.example.julia.weatherguide.presentation.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.data.services.refresh.RefreshDatabaseManager;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_preferences);

        findPreference(getString(R.string.key_refresh)).setOnPreferenceChangeListener(
            (preference, newValue) -> {
                if (newValue instanceof String) {
                    String[] periods = getResources().getStringArray(R.array.refresh_intervals_identifiers);
                    int periodIndex = -1;
                    for (int i = 0; i < periods.length; ++i) {
                        if (periods[i].equals(newValue)) {
                            periodIndex = i;
                            break;
                        }
                    }
                    if (periodIndex == -1) throw new IllegalStateException("New period value must be defined");

                    RefreshDatabaseManager.Period period = RefreshDatabaseManager.getPeriod(periodIndex);
                    RefreshDatabaseManager.setCurrentRefreshPolicy(period);
                    return true;
                } else {
                    throw new IllegalStateException("New value can not be non String");
                }
            });
    }
}
