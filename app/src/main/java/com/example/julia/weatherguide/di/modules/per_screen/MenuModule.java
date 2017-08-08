package com.example.julia.weatherguide.di.modules.per_screen;

import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.domain.use_cases.AddLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.DeleteLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.SubscribeOnLocationChangesUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.menu.MenuPresenter;
import com.example.julia.weatherguide.presentation.menu.MenuView;

import dagger.Module;
import dagger.Provides;


@Module
public class MenuModule {

    @Provides
    @PerScreen
    PresenterFactory<MenuPresenter, MenuView> getPresenterFactory(
        SubscribeOnLocationChangesUseCase subscribeOnLocationChangesUseCase,
        DeleteLocationUseCase deleteLocationUseCase,
        AddLocationUseCase addLocationUseCase
    ) {
        return new MenuPresenter.Factory(subscribeOnLocationChangesUseCase, deleteLocationUseCase,
            addLocationUseCase);
    }

}
