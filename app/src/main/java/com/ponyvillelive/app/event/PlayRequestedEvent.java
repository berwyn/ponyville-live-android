package com.ponyvillelive.app.event;

import com.ponyvillelive.app.model.Station;

/**
 * Created by tyr on 13/05/2014.
 */
public class PlayRequestedEvent {

    public final Station station;

    public PlayRequestedEvent(Station station) {
        this.station = station;
    }
}
