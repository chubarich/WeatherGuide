package com.example.julia.weatherguide.ui.current_weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.repositories.CurrentWeatherDataModel;
import com.example.julia.weatherguide.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by julia on 15.07.17.
 */

public class CurrentWeatherFragment extends BaseFragment implements CurrentWeatherView {

    private static final String TAG = CurrentWeatherFragment.class.getSimpleName();

    @BindView(R.id.tv_current_degrees)
    TextView currentDegreesTextView;

    @BindView(R.id.pb_loading)
    ProgressBar loadingProgressBar;

    @BindView(R.id.tv_humidity)
    TextView humidityTextView;

    @BindView(R.id.bn_refresh_current_weather)
    Button refreshButton;

    @BindView(R.id.iv_weather_icon)
    ImageView weatherIconImageView;

    @BindView(R.id.tv_location_name)
    TextView locationNameTextView;

    @BindView(R.id.tv_humidity_title)
    TextView humidityTitle;

    @BindView(R.id.tv_weather_description)
    TextView weatherDescriptionTextView;

    @BindView(R.id.tv_empty_view)
    TextView emptyTextView;

    private Unbinder unbinder;

    @Inject
    CurrentWeatherPresenter presenter;


    public static CurrentWeatherFragment newInstance() {
        final CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        return fragment;
    }

    @Override
    public void showLoading() {
        refreshButton.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
        refreshButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        Snackbar.make(CurrentWeatherFragment.this.currentDegreesTextView, getString(R.string.network_error_description), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyView() {
        currentDegreesTextView.setVisibility(View.GONE);
        humidityTextView.setVisibility(View.GONE);
        humidityTitle.setVisibility(View.GONE);
        weatherIconImageView.setVisibility(View.GONE);
        locationNameTextView.setVisibility(View.GONE);
        weatherDescriptionTextView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
    }

    private void showMainContent() {
        emptyTextView.setVisibility(View.INVISIBLE);
        currentDegreesTextView.setVisibility(View.VISIBLE);
        humidityTextView.setVisibility(View.VISIBLE);
        humidityTitle.setVisibility(View.VISIBLE);
        weatherIconImageView.setVisibility(View.VISIBLE);
        locationNameTextView.setVisibility(View.VISIBLE);
        weatherDescriptionTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void showData(@NonNull CurrentWeatherDataModel data) {
        showMainContent();
        currentDegreesTextView.setText(String.valueOf(data.getCurrentTemperature() + " " + getContext().getString(R.string.celcium_symbol)));
        humidityTextView.setText(" " + String.valueOf(data.getHumidity()) + "%");
        locationNameTextView.setText(data.getLocationName());
        weatherDescriptionTextView.setText(data.getWeatherDescription());
        weatherIconImageView.setImageBitmap(data.getIcon());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_weather_constr_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        WeatherGuideApplication.getInstance().plusScreenRelatedComponent().inject(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.left_drawer_menu_title_weather);
        presenter.attachView(this);
    }

    @OnClick(R.id.bn_refresh_current_weather)
    public void refreshCurrentWeather() {
        presenter.refreshCurrentWeather(getResources().getString(
                R.string.moscow_location_id
        ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

}
