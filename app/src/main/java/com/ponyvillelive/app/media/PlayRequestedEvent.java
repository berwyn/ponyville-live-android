package com.ponyvillelive.app.media;

/**
 * Created by tyr on 13/05/2014.
 */
public class PlayRequestedEvent {

    public final String stationUrl;

    public PlayRequestedEvent(String stationUrl) {
        this.stationUrl = stationUrl;
    }
}
