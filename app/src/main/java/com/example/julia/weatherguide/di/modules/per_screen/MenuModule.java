package com.example.julia.weatherguide.di.modules.per_screen;

import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.domain.use_cases.AddLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.DeleteLocationUseCase;
import com.example.julia.weatherguide.domain.use_cases.GetLocationsAndSubscribeUseCase;
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
        GetLocationsAndSubscribeUseCase getLocationsAndSubscribeUseCase,
        DeleteLocationUseCase deleteLocationUseCase,
        AddLocationUseCase addLocationUseCase
    ) {
        return new MenuPresenter.Factory(getLocationsAndSubscribeUseCase, deleteLocationUseCase,
            addLocationUseCase);
    }

}
