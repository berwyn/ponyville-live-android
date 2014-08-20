package com.ponyvillelive.app.net;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.DebugData;
import com.ponyvillelive.app.model.NowPlayingResponse;
import com.ponyvillelive.app.model.NowPlayingStationResponse;
import com.ponyvillelive.app.model.ShowResponse;
import com.ponyvillelive.app.model.StationResponse;

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
    public Observable<StationResponse> getStationList() {
        return Observable.from(data.stations.get("all"));
    }

    @Override
    public Observable<StationResponse> getStationList(@Path("category") String category) {
        return Observable.from(data.stations.get(category));
    }

    @Override
    public Observable<NowPlayingResponse> getNowPlaying() {
        return Observable.from(data.nowPlaying);
    }

    @Override
    public Observable<NowPlayingStationResponse> getNowPlayingForStation(@Path("id") int id) {
        return null;
    }

    @Override
    public Observable<ShowResponse> getShows() {
        return null;
    }

    @Override
    public Observable<ShowResponse> getAllShows() {
        return null;
    }

    @Override
    public Observable<ShowResponse> getEpisodesForShow(@Path("id") String id) {
        return null;
    }
}
