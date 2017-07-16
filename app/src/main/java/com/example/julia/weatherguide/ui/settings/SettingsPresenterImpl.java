package com.example.julia.weatherguide.ui.settings;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.ui.base.BasePresenter;

/**
 * Created by julia on 16.07.17.
 */

public class SettingsPresenterImpl extends BasePresenter<SettingsView> {

    private final SettingsInteractor interactor;
    private Context context;

    public SettingsPresenterImpl(SettingsInteractor interactor, @NonNull Context context) {
        this.interactor = interactor;
        this.context = context;
    }

    void onRefreshPeriodChanged(String newValue) {
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
