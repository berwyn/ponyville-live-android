package com.ponyvillelive.pvlmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by tyr on 22/05/2014.
 */
public class NowPlayingMeta {

    public Station station;
    public Map<String, Integer> listeners;
    @SerializedName("current_song")
    public Song currentSong;
    @SerializedName("song_history")
    public List<SongWrapper> songHistory;

}
