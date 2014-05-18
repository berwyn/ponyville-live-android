package com.ponyvillelive.app.event;

import com.ponyvillelive.app.model.Station;

/**
 * Event fired when a component wants to request playback of a given station
 */
public class PlayRequestedEvent {

    public final Station station;

    public PlayRequestedEvent(Station station) {
        this.station = station;
    }
}
