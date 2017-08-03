package com.example.julia.weatherguide.presentation.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.widget.Toast;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.presentation.application.WeatherGuideApplication;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.view.BasePreferenceFragment;
import com.example.julia.weatherguide.utils.PlacePickerHelper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import javax.inject.Inject;
import javax.inject.Provider;

public class SettingsFragment extends BasePreferenceFragment<SettingsPresenter, SettingsView>
    implements SettingsView {

    @Inject
    Provider<PresenterFactory<SettingsPresenter, SettingsView>> presenterFactoryProvider;

    // --------------------------------------- static -----------------------------------------------

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    // -------------------------------------- lifecycle ---------------------------------------------

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ((WeatherGuideApplication) getActivity().getApplication())
            .getSettingsComponent()
            .inject(this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_preferences);
        findPreference(getString(R.string.refresh_key)).setOnPreferenceChangeListener(
            (Preference preference, Object newValue) -> {
                ((SettingsPresenter) getPresenter()).onRefreshPeriodChanged((String) newValue);
                return false;
            });
        findPreference(getString(R.string.pick_city_key)).setOnPreferenceClickListener(
            (Preference preference) -> {
                pickCity();
                return false;
            }
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.left_drawer_menu_title_settings);
    }

    // ---------------------------------------- SettingsView ----------------------------------------

    @Override
    public void showNumberFormatError() {
        Toast.makeText(getContext(), getString(R.string.wrong_time_value), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPickTimeSuccess() {
        Toast.makeText(getContext(), getString(R.string.interval_picked_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLocationNotPickedError() {
        Toast.makeText(getContext(), getString(R.string.city_not_picked_before_interval), Toast.LENGTH_SHORT).show();
    }

    // ------------------------------------ BasePreferenceFragment ----------------------------------

    @Override
    protected SettingsView getViewInterface() {
        return this;
    }

    @Override
    protected PresenterFactory<SettingsPresenter, SettingsView> getPresenterFactory() {
        return presenterFactoryProvider.get();
    }

    @Override
    protected int getFragmentId() {
        return SettingsFragment.class.getSimpleName().hashCode();
    }

    // ------------------------------------------ private -------------------------------------------

    private void pickCity() {
        if (getActivity() != null) {
            try {
                AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();

                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(autocompleteFilter)
                    .build(getActivity());
                getActivity().startActivityForResult(intent, PlacePickerHelper.CODE_PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                Toast.makeText(getContext(), getString(R.string.play_services_not_available), Toast.LENGTH_SHORT)
                    .show();
            }
        }
    }
}
