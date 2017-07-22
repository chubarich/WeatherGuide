package com.example.julia.weatherguide.ui.settings;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.Toast;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.ui.base.view.BasePreferenceFragment;

import javax.inject.Inject;
import javax.inject.Provider;

import static android.support.v7.preference.R.layout.preference;

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
    super.onActivityCreated(savedInstanceState);
    ((WeatherGuideApplication) getActivity().getApplication())
        .getSettingsComponent()
        .inject(this);
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.fragment_preferences);
    findPreference(getString(R.string.refresh_key))
        .setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
          ((SettingsPresenter) getPresenter()).onRefreshPeriodChanged((String) newValue);
          return false;
        });
  }

  @Override
  public void onStart() {
    super.onStart();
    getActivity().setTitle(R.string.left_drawer_menu_title_settings);
  }

  // ---------------------------------------- SettingsView ----------------------------------------

  @Override
  public void showError() {
    Toast.makeText(getContext(), getString(R.string.wrong_time_value), Toast.LENGTH_SHORT).show();
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

}
