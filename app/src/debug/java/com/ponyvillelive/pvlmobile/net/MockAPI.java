package com.ponyvillelive.pvlmobile.net;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ponyvillelive.pvlmobile.R;
import com.ponyvillelive.pvlmobile.model.net.ArrayResponse;
import com.ponyvillelive.pvlmobile.model.DebugData;
import com.ponyvillelive.pvlmobile.model.net.MapResponse;
import com.ponyvillelive.pvlmobile.model.NowPlayingMeta;
import com.ponyvillelive.pvlmobile.model.net.ObjectResponse;
import com.ponyvillelive.pvlmobile.model.Show;
import com.ponyvillelive.pvlmobile.model.Station;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import retrofit.http.Path;
import rx.Observable;

/**
 * A mock implementation of the PVL API
 */
public class MockAPI implements API {

    private DebugData data;

    @Inject
    public MockAPI(Application application) {
        InputStream is = application.getResources().openRawResource(R.raw.debug_json);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        Gson gson = new GsonBuilder().create();
        this.data = gson.fromJson(br, DebugData.class);
    }

    @Override
    public Observable<ArrayResponse<Station>> getStationList() {
        return Observable.just(data.stations.get("all"));
    }

    @Override
    public Observable<ArrayResponse<Station>> getStationList(@Path("category") String category) {
        return Observable.just(data.stations.get(category));
    }

    @Override
    public Observable<MapResponse<String, NowPlayingMeta>> getNowPlaying() {
        return Observable.just(data.nowPlaying);
    }

    @Override
    public Observable<ObjectResponse<NowPlayingMeta>> getNowPlayingForStation(@Path("id") int id) {
        return null;
    }

    @Override
    public Observable<ArrayResponse<Show>> getShows() {
        return null;
    }

    @Override
    public Observable<ArrayResponse<Show>> getAllShows() {
        return null;
    }

    @Override
    public Observable<ObjectResponse<Show>> getEpisodesForShow(@Path("id") String id) {
        return null;
    }
}
