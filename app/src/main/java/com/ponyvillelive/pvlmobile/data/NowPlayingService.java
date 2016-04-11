package com.ponyvillelive.pvlmobile.data;

import android.content.Context;

import com.ponyvillelive.pvlmobile.PvlApp;
import com.ponyvillelive.pvlmobile.model.NowPlayingMeta;
import com.ponyvillelive.pvlmobile.net.API;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by berwyn on 10/04/2016.
 */
public final class NowPlayingService {

    private static NowPlayingService INSTANCE = null;

    @Inject()
    API api;

    private Observable<Map<String, NowPlayingMeta>> nowPlayingMeta;

    public static NowPlayingService getInstance(Context ctxt) {
        if(INSTANCE == null) {
            INSTANCE = new NowPlayingService(ctxt);
        }
        return INSTANCE;
    }

    private NowPlayingService(Context ctxt) {
        PvlApp app = PvlApp.get(ctxt);
        app.inject(this);
    }

}
