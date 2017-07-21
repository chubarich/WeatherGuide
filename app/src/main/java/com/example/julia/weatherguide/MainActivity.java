package com.example.julia.weatherguide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.julia.weatherguide.ui.base.BaseActivity;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherFragment;
import com.example.julia.weatherguide.ui.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nv_main) NavigationView mDrawerMenu;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @State
    int mCurrentFragmentId = Integer.MAX_VALUE;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, CurrentWeatherFragment.newInstance())
                    .commit();
            mCurrentFragmentId = R.id.action_to_current_weather;
        }
        mDrawerMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == mCurrentFragmentId) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                switch (id) {
                    case R.id.action_to_current_weather:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_content, CurrentWeatherFragment.newInstance())
                                .commit();
                        break;
                    case R.id.action_to_settings:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_content, SettingsFragment.newInstance())
                                .commit();
                        break;
                    case R.id.action_to_about:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_content, AboutFragment.newInstance())
                                .commit();
                        break;
                }
                mCurrentFragmentId = id;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
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
}
