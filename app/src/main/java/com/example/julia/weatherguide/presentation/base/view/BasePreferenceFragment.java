package com.example.julia.weatherguide.presentation.base.view;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterLoader;

public abstract class BasePreferenceFragment<P extends BasePresenter<V>, V extends BaseView>
    extends PreferenceFragmentCompat implements BaseView, LoaderManager.LoaderCallbacks<P> {

    private P presenter;

    protected final BasePresenter<V> getPresenter() {
        return presenter;
    }

    // -------------------------------------- lifecycle ---------------------------------------------

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // loader is created -> get presenter
        Loader<P> loader = getLoaderManager().getLoader(getFragmentId());
        if (loader != null) {
            presenter = ((PresenterLoader<P, V>) loader).getPresenter();
        }

        // init loader
        if (presenter == null) {
            getLoaderManager().initLoader(getFragmentId(), null, this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(getViewInterface());
    }

    @Override
    public void onStop() {
        presenter.detachView();
        super.onStop();
    }

    // ------------------------------------ LoaderCallbacks ---------------------------------------

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(getContext(), getPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        this.presenter = data;
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        this.presenter = null;
    }

    // ---------------------------------------- abstract --------------------------------------------

    protected abstract V getViewInterface();

    protected abstract PresenterFactory<P, V> getPresenterFactory();

    protected abstract int getFragmentId();

}
