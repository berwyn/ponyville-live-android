package com.ponyvillelive.app.event;

import com.ponyvillelive.app.model.Station;

/**
 * Event fired when the last sent {@link com.ponyvillelive.app.event.PlayRequestedEvent} is
 * fulfilled.
 */
public class PlaybackStartedEvent {

    public final Station station;

    public PlaybackStartedEvent(Station station) {
        this.station = station;
    }

}
