package com.example.julia.weatherguide.di.modules.per_screen;

import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.domain.use_cases.GetLocationFromPredictionUseCase;
import com.example.julia.weatherguide.domain.use_cases.GetLocationPredictionsUseCase;
import com.example.julia.weatherguide.presentation.base.presenter.PresenterFactory;
import com.example.julia.weatherguide.presentation.choose_location.ChooseLocationPresenter;
import com.example.julia.weatherguide.presentation.choose_location.ChooseLocationView;

import dagger.Module;
import dagger.Provides;


@Module
public class ChooseLocationModule {

    @Provides
    @PerScreen
    PresenterFactory<ChooseLocationPresenter, ChooseLocationView> getPresenterFactory(
        GetLocationPredictionsUseCase getLocationPredictionsUseCase,
        GetLocationFromPredictionUseCase getLocationFromPredictionUseCase
    ) {
        return new ChooseLocationPresenter.Factory(getLocationPredictionsUseCase,
            getLocationFromPredictionUseCase);
    }

}
