package com.example.julia.weatherguide.ui.settings;

import android.content.Context;

import com.example.julia.weatherguide.R;
import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.ui.base.presenter.BasePresenter;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;

import javax.inject.Inject;

public class SettingsPresenter extends BasePresenter<SettingsView> {

  private final SettingsInteractor interactor;


  private SettingsPresenter(SettingsInteractor interactor) {
    this.interactor = interactor;
  }

  // ------------------------------------ BasePresenter -------------------------------------------

  @Override
  protected void onViewAttached() {
  }

  @Override
  protected void onViewDetached() {

  }

  @Override
  protected void onDestroyed() {
    interactor.destroy();
  }

  // ------------------------------------------- public -------------------------------------------

  void onRefreshPeriodChanged(String newValue) {
    try {
      long period = Long.valueOf(newValue);
      if (period > 0) {
        interactor.saveRefreshPeriod(period);
      } else {
        getView().showError();
      }
    } catch (NumberFormatException e) {
      getView().showError();
    }
  }

  // ------------------------------------------ Factory -------------------------------------------

  public static class Factory implements PresenterFactory<SettingsPresenter, SettingsView> {

    private final SettingsInteractor settingsInteractor;

    public Factory(SettingsInteractor settingsInteractor) {
      this.settingsInteractor = settingsInteractor;
    }

    @Override
    public SettingsPresenter create() {
      return new SettingsPresenter(settingsInteractor);
    }
  }
}
