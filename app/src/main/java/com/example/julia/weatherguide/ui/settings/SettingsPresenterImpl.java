package com.example.julia.weatherguide.ui.settings;

import android.content.Context;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.ui.base.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by julia on 16.07.17.
 */

public class SettingsPresenterImpl extends BasePresenterImpl<SettingsView> implements SettingsPresenter<SettingsView> {

    @Inject
    SettingsInteractor interactor;

    @Inject
    Context context;

    public SettingsPresenterImpl() {
        WeatherGuideApplication.getDataComponent().inject(this);
    }


    public void onRefreshPeriodChanged(String newValue) {
        try {
            long period = Long.valueOf(newValue);
            if (period > 0) {
                interactor.saveRefreshPeriod(period);
            } else {
                getView().showError(context.getString(R.string.wrong_time_value));
            }
        } catch (NumberFormatException e) {
            getView().showError(context.getString(R.string.wrong_time_value));
        }


    }
}
