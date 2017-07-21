package com.example.julia.weatherguide.ui.settings;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractorImpl;
import com.example.julia.weatherguide.interactors.SettingsInteractorImpl;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepository;
import com.example.julia.weatherguide.repositories.CurrentWeatherRepositoryImpl;
import com.example.julia.weatherguide.repositories.SettingsRepository;
import com.example.julia.weatherguide.repositories.SettingsRepositoryImpl;

/**
 * Created by julia on 09.07.17.
 */

public class SettingsFragment extends PreferenceFragmentCompat  implements SettingsView {

    private SettingsPresenterImpl presenter;

    public static SettingsFragment newInstance() {
        final SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_preferences);
        findPreference(getString(R.string.refresh_key)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                presenter.onRefreshPeriodChanged((String) newValue);
                return false;
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.left_drawer_menu_title_settings);
        CurrentWeatherRepository weatherRepo = new CurrentWeatherRepositoryImpl();
        CurrentWeatherInteractor weatherInteractor = new CurrentWeatherInteractorImpl(getContext(), weatherRepo );
        SettingsRepository settingsRepo = new SettingsRepositoryImpl(getContext());
        presenter = new SettingsPresenterImpl(new SettingsInteractorImpl(settingsRepo, weatherInteractor), getContext());
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void showError(String error) {
        //TODO:change to dialog
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
