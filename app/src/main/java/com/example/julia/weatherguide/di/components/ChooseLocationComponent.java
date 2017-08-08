package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.per_screen.ChooseLocationModule;
import com.example.julia.weatherguide.di.modules.per_screen.SchedulerModule;
import com.example.julia.weatherguide.di.modules.per_screen.UseCasesModule;
import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.presentation.choose_location.ChooseLocationActivity;

import dagger.Subcomponent;


@Subcomponent(modules = {SchedulerModule.class, UseCasesModule.class, ChooseLocationModule.class})
@PerScreen
public interface ChooseLocationComponent {

    void inject(ChooseLocationActivity chooseLocationActivity);

}
