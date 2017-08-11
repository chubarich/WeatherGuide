package com.example.julia.weatherguide.presentation.main;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.presentation.application.WeatherGuideApplication;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.view.BaseActivity;
import com.example.julia.weatherguide.presentation.weather.WeatherFragment;
import com.example.julia.weatherguide.presentation.menu.MenuView;

import javax.inject.Inject;
import javax.inject.Provider;


public class MainActivity extends BaseActivity<MainPresenter, MainView> implements MainView, DrawerView {

    private FrameLayout fragmentContainer;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private boolean drawerMode;

    @Inject
    Provider<PresenterFactory<MainPresenter, MainView>> presenterFactoryProvider;

    // ------------------------------------------ lifecycle -----------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((WeatherGuideApplication)getApplication())
            .getMainComponent()
            .inject(this);

        super.onCreate(savedInstanceState);
        initializeView(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerMode) drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerMode && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerMode && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    // -------------------------------------- DrawerView ------------------------------------------

    @Override
    public void closeDrawer() {
        if (drawerMode) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void setTitle(String title) {
        if (drawerMode && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    // --------------------------------------- MainView -------------------------------------------

    @Override
    public void onCurrentLocationChanged(LocationWithId newLocation) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, WeatherFragment.newInstance(newLocation))
            .commit();
    }

    // -------------------------------------- BaseActivity ----------------------------------------

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
        return this.getClass().getSimpleName().hashCode();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    // ---------------------------------------- private ---------------------------------------------

    private void initializeView(Bundle savedInstanceState) {
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            drawerMode = true;
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
            drawerLayout.addDrawerListener(drawerToggle);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_menu);
                    if (fragment != null) {
                        if (fragment instanceof MenuView) {
                            ((MenuView) fragment).onDrawerClosed();
                        } else {
                            throw new IllegalStateException("Fragment with id fragment_menu must be instanceof MenuView");
                        }

                    }
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                }
            });
        }
    }
}
