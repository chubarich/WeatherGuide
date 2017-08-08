package com.example.julia.weatherguide.presentation.current_weather;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.weather.CurrentWeather;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.view.BaseFragment;


public class CurrentWeatherFragment extends BaseFragment<CurrentWeatherPresenter, CurrentWeatherView>
    implements CurrentWeatherView {

    private State state;

    // -------------------------------------- newInstance -----------------------------------------

    public static CurrentWeatherFragment newInstance(Location location) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        fragment.setArguments(State.wrap(location));
        return fragment;
    }

    // -------------------------------------- lifecycle -------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        state = new State(savedInstanceState);
        if (!state.isInitialized()) {
            state = new State(getArguments());
        }

        super.onCreate(savedInstanceState);
    }

    // ------------------------------------- BaseFragment -----------------------------------------

    @Override
    protected CurrentWeatherView getViewInterface() {
        return this;
    }

    @Override
    protected PresenterFactory<CurrentWeatherPresenter, CurrentWeatherView> getPresenterFactory() {
        return null;
    }

    @Override
    protected int getFragmentId() {
        return 0;
    }

    @Override
    protected int getLayoutRes() {
        return 0;
    }

    // ------------------------------------ inner types -------------------------------------------

    private static class State {

        private static final String KEY_LOCATION = "CURRENT_WEATHER_STATE_KEY_LOCATION";
        private final Location location;

        public State(Bundle bundle) {
            if (bundle.containsKey(KEY_LOCATION)) {
                location = bundle.getParcelable(KEY_LOCATION);
            } else {
                location = null;
            }
        }

        public boolean isInitialized() {
            return location != null;
        }

        public static Bundle wrap(Location location) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_LOCATION, location);
            return bundle;
        }

    }
}
