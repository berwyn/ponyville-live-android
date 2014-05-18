package com.ponyvillelive.app.ui;

import android.app.Fragment;

import com.ponyvillelive.app.model.Station;

/**
 * Created by tyr on 17/05/2014.
 */
public enum NavigationEnum {
    RADIO(() -> StationFragment.newInstance(Station.STATION_TYPE_AUDIO)),
    VIDEO(() -> StationFragment.newInstance(Station.STATION_TYPE_VIDEO)),
    SHOWS(ShowFragment::newInstance),
    REQUESTS(RequestFragment::newInstance);

    private interface NavigationFunction {
        public Fragment getFragment();
    }

    private NavigationFunction navigationFunction;

    NavigationEnum(NavigationFunction navigationFunction) {
        this.navigationFunction = navigationFunction;
    }

    public Fragment getFragment() {
        return navigationFunction.getFragment();
    }
}
