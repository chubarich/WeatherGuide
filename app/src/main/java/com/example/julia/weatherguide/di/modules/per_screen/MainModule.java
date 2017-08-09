package com.example.julia.weatherguide.di.modules.per_screen;

import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.domain.use_cases.SubscribeOnCurrentLocationUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.main.MainPresenter;
import com.example.julia.weatherguide.presentation.main.MainView;

import dagger.Module;
import dagger.Provides;


@Module
public class MainModule {

    @Provides
    @PerScreen
    PresenterFactory<MainPresenter, MainView> getPresenterFactory(
        SubscribeOnCurrentLocationUseCase subscribeOnCurrentLocationUseCase
    ) {
        return new MainPresenter.Factory(subscribeOnCurrentLocationUseCase);
    }

}
