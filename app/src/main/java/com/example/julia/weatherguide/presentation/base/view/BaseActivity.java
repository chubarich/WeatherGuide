package com.example.julia.weatherguide.presentation.base.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.example.julia.weatherguide.presentation.base.presenter.BasePresenter;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterLoader;


public abstract class BaseActivity<P extends BasePresenter<V>, V extends BaseView>
    extends AppCompatActivity implements BaseView, LoaderManager.LoaderCallbacks<P> {

    private P presenter;

    protected final BasePresenter<V> getPresenter() {
        return presenter;
    }

    // -------------------------------------- lifecycle -------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());

        // loader is created -> get presenter
        Loader<P> loader = getSupportLoaderManager().getLoader(getActivityId());
        if (loader != null) {
            presenter = ((PresenterLoader<P, V>) loader).getPresenter();
        }

        // init loader
        if (presenter == null) {
            getSupportLoaderManager().initLoader(getActivityId(), null, this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(getViewInterface());
    }

    @Override
    protected void onStop() {
        presenter.detachView();
        super.onStop();
    }

    // ------------------------------------ LoaderCallbacks ---------------------------------------

    @Override
    public Loader<P> onCreateLoader(int i, Bundle bundle) {
        return new PresenterLoader<>(this, getPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        this.presenter = null;
    }

    // --------------------------------------- abstract ---------------------------------------------

    protected abstract V getViewInterface();

    protected abstract PresenterFactory<P, V> getPresenterFactory();

    protected abstract int getActivityId();

    @LayoutRes
    protected abstract int getLayoutRes();

}
