package com.example.julia.weatherguide.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.ui.about.AboutFragment;
import com.example.julia.weatherguide.ui.base.view.BaseActivity;
import com.example.julia.weatherguide.ui.current_weather.CurrentWeatherFragment;
import com.example.julia.weatherguide.ui.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

  @BindView(R.id.drawer_layout)
  DrawerLayout mDrawerLayout;
  @BindView(R.id.nv_main)
  NavigationView mDrawerMenu;
  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @State
  int mCurrentFragmentId = Integer.MAX_VALUE;

  private ActionBarDrawerToggle mDrawerToggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Icepick.restoreInstanceState(this, savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setupListeners(savedInstanceState);
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

  private void setupListeners(Bundle savedInstanceState) {
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
}
