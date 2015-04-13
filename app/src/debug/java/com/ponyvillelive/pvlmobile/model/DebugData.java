package com.ponyvillelive.pvlmobile.model;

import com.ponyvillelive.pvlmobile.model.net.ArrayResponse;
import com.ponyvillelive.pvlmobile.model.net.MapResponse;

import java.util.Map;

/**
 * Created by berwyn on 19/08/14.
 */
public class DebugData {

    public Map<String, ArrayResponse<Station>> stations;
    public MapResponse<String, NowPlayingMeta> nowPlaying;

}
