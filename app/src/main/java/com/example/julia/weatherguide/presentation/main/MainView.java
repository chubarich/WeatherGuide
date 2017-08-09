package com.example.julia.weatherguide.presentation.main;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.presentation.base.view.BaseView;


public interface MainView extends BaseView {

    void onCurrentLocationChanged(Location newLocation);

}
