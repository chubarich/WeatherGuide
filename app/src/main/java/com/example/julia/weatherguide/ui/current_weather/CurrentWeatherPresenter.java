package com.example.julia.weatherguide.ui.current_weather;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.WeatherGuideApplication;
import com.example.julia.weatherguide.interactors.CurrentWeatherInteractor;
import com.example.julia.weatherguide.interactors.SettingsInteractor;
import com.example.julia.weatherguide.repositories.data.CurrentWeatherDataModel;
import com.example.julia.weatherguide.ui.base.presenter.BasePresenter;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.ui.settings.SettingsPresenter;
import com.example.julia.weatherguide.ui.settings.SettingsView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CurrentWeatherPresenter extends BasePresenter<CurrentWeatherView> {

  private CurrentWeatherViewState viewState = new CurrentWeatherViewState();

  private final CurrentWeatherInteractor currentWeatherInteractor;
  private CompositeDisposable loadCurrentWeatherDisposable;
  private CompositeDisposable refreshCurrentWeatherDisposable;


  private CurrentWeatherPresenter(CurrentWeatherInteractor currentWeatherInteractor) {
    this.currentWeatherInteractor = currentWeatherInteractor;
    loadCurrentWeatherDisposable = new CompositeDisposable();
    refreshCurrentWeatherDisposable = new CompositeDisposable();
  }

  // --------------------------------------- BasePresenter ----------------------------------------

  @Override
  protected void onViewAttached() {
    if (viewState.isLoading()) {
      if (viewState.isReasonForLoadingUpdate()) {
        refreshCurrentWeather(viewState.getLocation());
      } else {
        loadCurrentWeatherForLocation(viewState.getLocation());
      }
    } else {
      if (viewState.hasData()) {
        getView().showData(viewState.getData());
      } else {
        loadCurrentWeatherForLocation(currentWeatherInteractor.getCurrentLocation());
      }
    }
  }

  @Override
  protected void onViewDetached() {

  }

  @Override
  protected void onDestroyed() {
    currentWeatherInteractor.destroy();
    loadCurrentWeatherDisposable.dispose();
    refreshCurrentWeatherDisposable.dispose();
  }

  // ---------------------------------------- public ----------------------------------------------

  void refreshCurrentWeather(@NonNull String location) {
    prepareViewForRefreshData(location);
    viewState.setReasonForLoadingUpdate(true);
    Disposable disposable = currentWeatherInteractor.getFreshCurrentWeatherForLocation(location)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::successGetDataAction, this::errorGetDataAction);
    loadCurrentWeatherDisposable.add(disposable);
  }

  // ----------------------------------------- private --------------------------------------------

  private void loadCurrentWeatherForLocation(@NonNull String location) {
    prepareViewForRefreshData(location);
    Disposable disposable = currentWeatherInteractor.getCurrentWeatherForLocation(location)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::successGetDataAction,
            error -> {
              if (isViewAttached()) {
                if (viewState.hasData()) {
                  errorGetDataAction(error);
                } else {
                  getView().hideLoading();
                  viewState.setLoading(false);
                  getView().showEmptyView();
                }
              }
            });
    loadCurrentWeatherDisposable.add(disposable);
  }

  private void successGetDataAction(CurrentWeatherDataModel data) {
    if (isViewAttached()) {
      getView().hideLoading();
      viewState.setLoading(false);
      viewState.setData(data);
      getView().showData(data);
    }
  }

  private void errorGetDataAction(Throwable error) {
    if (isViewAttached()) {
      getView().hideLoading();
      viewState.setLoading(false);
      getView().showError();
    }
  }

  private void prepareViewForRefreshData(@NonNull String location) {
    if (isViewAttached()) {
      getView().showLoading();
      viewState.setLoading(true);
      viewState.setLocation(location);
    }
  }

  // ---------------------------------------- inner types ----------------------------------------

  private class CurrentWeatherViewState {

    private boolean isLoading;
    private boolean hasData;
    private String location;
    private boolean reasonForLoadingUpdate = false;
    private CurrentWeatherDataModel data;


    private CurrentWeatherViewState() {
    }


    private boolean isLoading() {
      return isLoading;
    }

    private void setLoading(boolean loading) {
      isLoading = loading;
    }

    private String getLocation() {
      return location;
    }

    private void setLocation(String location) {
      this.location = location;
    }

    private boolean isReasonForLoadingUpdate() {
      return reasonForLoadingUpdate;
    }

    private void setReasonForLoadingUpdate(boolean reasonForLoadingUpdate) {
      this.reasonForLoadingUpdate = reasonForLoadingUpdate;
    }

    public CurrentWeatherDataModel getData() {
      return data;
    }

    public void setData(CurrentWeatherDataModel data) {
      this.hasData = true;
      this.data = data;
    }

    private boolean hasData() {
      return hasData;
    }

  }

  // ------------------------------------------ Factory -------------------------------------------

  public static class Factory implements PresenterFactory<CurrentWeatherPresenter, CurrentWeatherView> {

    private final CurrentWeatherInteractor currentWeatherInteractor;

    public Factory(CurrentWeatherInteractor currentWeatherInteractor) {
      this.currentWeatherInteractor = currentWeatherInteractor;
    }

    @Override
    public CurrentWeatherPresenter create() {
      return new CurrentWeatherPresenter(currentWeatherInteractor);
    }
  }
}
