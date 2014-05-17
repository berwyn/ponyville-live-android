package com.ponyvillelive.app;

import com.squareup.otto.Bus;

/**
 * Maintain's the app's {@link com.squareup.otto.Bus} instance
 */
public class BusProvider {

    private static Bus bus;
    public static Bus getBus() {
        if(bus == null) bus = new Bus();
        return bus;
    }

}
