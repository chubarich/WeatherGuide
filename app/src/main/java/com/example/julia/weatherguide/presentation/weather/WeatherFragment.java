package com.example.julia.weatherguide.presentation.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.presentation.application.WeatherGuideApplication;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.view.BaseFragment;

import javax.inject.Inject;
import javax.inject.Provider;


public class WeatherFragment extends BaseFragment<WeatherPresenter, WeatherView>
    implements WeatherView {

    private State state;

    @Inject
    Provider<PresenterFactory<WeatherPresenter, WeatherView>> presenterFactoryProvider;

    // -------------------------------------- newInstance -----------------------------------------

    public static WeatherFragment newInstance(Location location) {
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(State.wrap(location));
        return fragment;
    }

    // -------------------------------------- lifecycle -------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        state = new State(savedInstanceState);
        if (state.location == null) {
            state = new State(getArguments());
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ((WeatherGuideApplication)getActivity().getApplication())
            .getWeatherComponent()
            .inject(this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView)view.findViewById(R.id.text_view);

        if (state.location != null) {
            textView.setText(state.location.name);
        } else {
            textView.setText("Город не инициализирован");
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

    // ------------------------------------ inner types -------------------------------------------

    private static class State {

        private static final String KEY_LOCATION = "CURRENT_WEATHER_STATE_KEY_LOCATION";
        private final Location location;

        private State(Bundle bundle) {
            if (bundle != null && bundle.containsKey(KEY_LOCATION)) {
                location = bundle.getParcelable(KEY_LOCATION);
            } else {
                location = null;
            }
        }

        private static Bundle wrap(Location location) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_LOCATION, location);
            return bundle;
        }

    }
}
