package com.example.julia.weatherguide;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nv_main) NavigationView mDrawerMenu;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    private int mCurrentFragmentId = Integer.MAX_VALUE;
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String CURRENT_FRAGMENT_ID_TAG = "current_fragment";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mFragmentManager = getSupportFragmentManager();

        mDrawerMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == mCurrentFragmentId) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                switch (id) {
                    case R.id.action_to_settings:
                        mCurrentFragment = new SettingsFragment();
                        FragmentTransaction settingsTransaction = mFragmentManager.beginTransaction();
                        settingsTransaction.replace(R.id.main_content, mCurrentFragment);
                        settingsTransaction.commit();
                        mCurrentFragmentId = id;
                        break;
                    case R.id.action_to_about:
                        mCurrentFragment = new AboutFragment();
                        FragmentTransaction aboutTransaction = mFragmentManager.beginTransaction();
                        aboutTransaction.replace(R.id.main_content, mCurrentFragment);
                        aboutTransaction.commit();
                        mCurrentFragmentId = id;
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        if (savedInstanceState != null) {
            mCurrentFragmentId = savedInstanceState.getInt(CURRENT_FRAGMENT_ID_TAG);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT_ID_TAG, mCurrentFragmentId);
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
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
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
