package com.ponyvillelive.app.event;

/**
 * Created by tyr on 28/05/2014.
 */
public class DataCacheChangedEvent {

    public static final String EVT_KEY_STATION_LIST_CHANGED = "station_list_changed";

    public String eventType;

    // Station List Event options
    public String stationType;

}
