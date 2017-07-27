package com.example.julia.weatherguide.ui.base.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.julia.weatherguide.ui.base.presenter.BasePresenter;
import com.example.julia.weatherguide.ui.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.ui.base.presenter.PresenterLoader;

import icepick.Icepick;

public abstract class BaseFragment<P extends BasePresenter<V>, V extends BaseView>
    extends Fragment implements BaseView, LoaderManager.LoaderCallbacks<P> {

    private P presenter;

    protected final BasePresenter<V> getPresenter() {
        return presenter;
    }

    // -------------------------------------- lifecycle ---------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

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

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        return inflater.inflate(getLayoutRes(), parent, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(getViewInterface());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
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

    protected abstract int getLayoutRes();

}
