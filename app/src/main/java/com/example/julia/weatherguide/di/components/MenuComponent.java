package com.example.julia.weatherguide.di.components;

import com.example.julia.weatherguide.di.modules.per_screen.MenuModule;
import com.example.julia.weatherguide.di.modules.per_screen.SchedulerModule;
import com.example.julia.weatherguide.di.modules.per_screen.UseCasesModule;
import com.example.julia.weatherguide.di.scopes.PerScreen;
import com.example.julia.weatherguide.presentation.menu.MenuFragment;

import dagger.Subcomponent;


@Subcomponent(modules = {SchedulerModule.class, UseCasesModule.class, MenuModule.class})
@PerScreen
public interface MenuComponent {

    void inject(MenuFragment menuFragment);

}
