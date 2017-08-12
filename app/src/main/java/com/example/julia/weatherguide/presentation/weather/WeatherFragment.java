package com.example.julia.weatherguide.presentation.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.presentation.application.WeatherGuideApplication;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.view.BaseFragment;
import com.example.julia.weatherguide.presentation.main.DrawerView;

import javax.inject.Inject;
import javax.inject.Provider;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class WeatherFragment extends BaseFragment<WeatherPresenter, WeatherView>
    implements WeatherView {

    private TextView textMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private State state;

    @Inject
    Provider<PresenterFactory<WeatherPresenter, WeatherView>> presenterFactoryProvider;

    // -------------------------------------- newInstance -----------------------------------------

    public static WeatherFragment newInstance(LocationWithId location) {
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(State.wrap(location));
        return fragment;
    }

    // -------------------------------------- lifecycle -------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        state = new State(savedInstanceState);
        if (state.locationWithId == null) {
            state = new State(getArguments());
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ((WeatherGuideApplication) getActivity().getApplication())
            .getWeatherComponent()
            .inject(this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view);
    }

    @Override
    public void onStart() {
        DrawerView drawerView = (DrawerView) getActivity();
        if (state.locationWithId == null) {
            drawerView.setTitle(getString(R.string.weather));
        } else {
            drawerView.setTitle(state.locationWithId.location.name);
        }

        if (recyclerView.getAdapter() != null) {
            ((WeatherModel)recyclerView.getAdapter()).bindResources(getResources());
        }

        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recyclerView.getAdapter() != null) {
            ((WeatherModel) recyclerView.getAdapter()).unbindResources();
        }
    }

    // ------------------------------------- WeatherView ------------------------------------------

    @Override
    public boolean isInitialized() {
        return state.locationWithId != null;
    }

    @Override
    public LocationWithId getLocation() {
        return state.locationWithId;
    }

    @Override
    public void showWeather(Weather weather) {
        if (state.locationWithId != null) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            textMessage.setVisibility(View.INVISIBLE);

            WeatherModel weatherModel = (WeatherModel) recyclerView.getAdapter();
            weatherModel.setWeather(weather);
        }
    }

    @Override
    public void showLoading() {
        if (state.locationWithId != null) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        if (state.locationWithId != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showNoInternet() {
        if (state.locationWithId != null) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            if (recyclerView.getVisibility() == View.VISIBLE) {
                Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            } else {
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(getString(R.string.network_error));
            }
        }
    }

    @Override
    public void showApiError() {
        if (state.locationWithId != null) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            if (recyclerView.getVisibility() == View.VISIBLE) {
                Toast.makeText(getContext(), getString(R.string.api_error), Toast.LENGTH_SHORT).show();
            } else {
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(getString(R.string.api_error));
            }
        }
    }

    // ------------------------------------- BaseFragment -----------------------------------------

    @Override
    protected WeatherView getViewInterface() {
        return this;
    }

    @Override
    protected PresenterFactory<WeatherPresenter, WeatherView> getPresenterFactory() {
        return presenterFactoryProvider.get();
    }

    @Override
    protected int getFragmentId() {
        return this.getClass().getSimpleName().hashCode();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_weather;
    }

    // -------------------------------------- private ---------------------------------------------

    private void initializeView(View view) {
        textMessage = (TextView) view.findViewById(R.id.text_message);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        textMessage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);

        if (state.locationWithId == null) {
            textMessage.setVisibility(View.VISIBLE);
            textMessage.setText(R.string.location_not_chosen);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new WeatherAdapter());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));

            swipeRefreshLayout.setOnRefreshListener(() -> {
                ((WeatherPresenter) getPresenter()).onRefresh();
            });
        }
    }

    // ------------------------------------ inner types -------------------------------------------

    private static class State {

        private static final String KEY_LOCATION = "CURRENT_WEATHER_STATE_KEY_LOCATION";
        private final LocationWithId locationWithId;

        private State(Bundle bundle) {
            if (bundle != null && bundle.containsKey(KEY_LOCATION)) {
                locationWithId = bundle.getParcelable(KEY_LOCATION);
            } else {
                locationWithId = null;
            }
        }

        private static Bundle wrap(LocationWithId location) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_LOCATION, location);
            return bundle;
        }

    }
}
