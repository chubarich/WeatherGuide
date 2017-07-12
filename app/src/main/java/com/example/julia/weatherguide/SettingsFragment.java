package com.example.julia.weatherguide;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by julia on 09.07.17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        final SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.left_drawer_menu_title_settings);
    }
}
