package com.example.julia.weatherguide.ui.base.presenter;

import android.support.annotation.NonNull;

import com.example.julia.weatherguide.ui.base.view.BaseView;

public abstract class BasePresenter<V extends BaseView> implements Presenter<V> {

  private V relatedView;

  // -------------------------------------- BasePresenter -------------------------------------

  protected final V getView() {
    return relatedView;
  }

  protected final boolean isViewAttached() {
    return relatedView != null;
  }

  // ---------------------------------------- Presenter ---------------------------------------

  @Override
  public final void attachView(@NonNull V view) {
    relatedView = view;
    onViewAttached();
  }

  @Override
  public final void detachView() {
    onViewDetached();
    relatedView = null;
  }

  @Override
  public void destroy() {
    onDestroyed();
  }

  // -------------------------------------------- abstract ----------------------------------------

  protected abstract void onViewAttached();

  protected abstract void onViewDetached();

  protected abstract void onDestroyed();

}
