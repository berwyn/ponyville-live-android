package com.ponyvillelive.app;

import com.ponyvillelive.app.event.DataCacheChangedEvent;
import com.ponyvillelive.app.model.Station;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by tyr on 28/05/2014.
 */
public class DataCache {

    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock(true);
    private static final Lock readLock = locker.readLock();
    private static final Lock writeLock = locker.writeLock();

    private static Map<String, Station[]> stationMap;

    static {
        stationMap = new HashMap<>(2);
    }

    public static void cacheStationList(String mode, Station[] stations) {
        writeLock.lock();
        stationMap.put(mode, stations);
        writeLock.unlock();

        DataCacheChangedEvent event = new DataCacheChangedEvent();
        event.eventType = DataCacheChangedEvent.EVT_KEY_STATION_LIST_CHANGED;
        event.stationType = mode;
        BusProvider.getBus().post(event);
    }

    public static Station[] retrieveStationList(String mode) {
        readLock.lock();
        Station[] stations = stationMap.get(mode);
        readLock.unlock();
        return stations;
    }
}
