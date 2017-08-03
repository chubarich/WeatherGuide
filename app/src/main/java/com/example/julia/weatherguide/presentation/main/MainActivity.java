package com.example.julia.weatherguide.presentation.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.presentation.application.WeatherGuideApplication;
import com.example.julia.weatherguide.presentation.about.AboutFragment;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.view.BaseActivity;
import com.example.julia.weatherguide.presentation.current_weather.CurrentWeatherFragment;
import com.example.julia.weatherguide.presentation.settings.SettingsFragment;
import com.example.julia.weatherguide.utils.PlacePickerHelper;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MainActivity extends BaseActivity<MainPresenter, MainView>
    implements MainView, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nv_main)
    NavigationView mDrawerMenu;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @State
    int mCurrentFragmentId = Integer.MAX_VALUE;

    @Inject
    Provider<PresenterFactory<MainPresenter, MainView>> presenterFactoryProvider;

    private ActionBarDrawerToggle mDrawerToggle;

    // ------------------------------------------ lifecycle -----------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inject presenter factory before onCreate as it is needed in base activity
        ((WeatherGuideApplication) getApplication())
            .getMainViewComponent()
            .inject(this);
        super.onCreate(savedInstanceState);
        // bind view
        ButterKnife.bind(this);
        // setup view listeners
        setupListeners(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id != mCurrentFragmentId) {
            replaceFragment(id);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PlacePickerHelper.CODE_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                ((MainPresenter) getPresenter()).onLocationChanged(
                    (float) place.getLatLng().longitude,
                    (float) place.getLatLng().latitude,
                    place.getName().toString()
                );
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR || resultCode == RESULT_CANCELED) {
                showLocationChangedException();
            }
        }
    }

    // ---------------------------------------- MainView --------------------------------------------

    @Override
    public void showLocationChangedSuccess() {
        Toast.makeText(this, getString(R.string.city_was_picked), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLocationChangedException() {
        Toast.makeText(this, getString(R.string.city_not_picked), Toast.LENGTH_SHORT).show();
    }

    // -------------------------------------- BaseActivity ------------------------------------------

    @Override
    protected MainView getViewInterface() {
        return this;
    }

    @Override
    protected PresenterFactory<MainPresenter, MainView> getPresenterFactory() {
        return presenterFactoryProvider.get();
    }

    @Override
    protected int getActivityId() {
        return MainActivity.class.getSimpleName().hashCode();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    // ---------------------------------------- private ---------------------------------------------

    private void replaceFragment(int id) {
        Fragment fragment;
        switch (id) {
            case R.id.action_to_current_weather:
                fragment = CurrentWeatherFragment.newInstance();
                break;
            case R.id.action_to_settings:
                fragment = SettingsFragment.newInstance();
                break;
            case R.id.action_to_about:
                fragment = AboutFragment.newInstance();
                break;
            default:
                throw new IllegalStateException("Unknown fragment id");
        }

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit();
        mCurrentFragmentId = id;
    }

    private void setupListeners(Bundle savedInstanceState) {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (savedInstanceState == null) {
            replaceFragment(R.id.action_to_current_weather);
            mDrawerMenu.setCheckedItem(R.id.action_to_current_weather);
        }
        mDrawerMenu.setNavigationItemSelectedListener(MainActivity.this);
    }
}
