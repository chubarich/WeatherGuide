package com.example.julia.weatherguide.ui.base.presenter;

import android.content.Context;
import android.support.v4.content.Loader;

import com.example.julia.weatherguide.ui.base.view.BaseView;

public class PresenterLoader<P extends BasePresenter<V>, V extends BaseView> extends Loader<P> {

    private final PresenterFactory<P, V> factory;
    private P presenter;


    public PresenterLoader(Context context, PresenterFactory<P, V> factory) {
        super(context);

        if (factory == null) {
            throw new IllegalStateException("BasePresenter factory must be non null");
        }

        this.factory = factory;
    }

    // ---------------------------------------- public ----------------------------------------------

    public final P getPresenter() {
        return presenter;
    }

    // ---------------------------------------- Loader ----------------------------------------------

    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        presenter = factory.create();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        if (presenter != null) {
            presenter.destroy();
        }

        presenter = null;
    }
}