package com.ponyvillelive.app.net;

import com.google.gson.Gson;
import com.ponyvillelive.app.model.ShowResponse;
import com.ponyvillelive.app.model.StationMetaResponse;
import com.ponyvillelive.app.model.StationResponse;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * <p>The code contract for the Ponyville Live! API as documented on
 * <a href="http://docs.ponyvillelive.apiary.io/">Apiary</a></p>
 */
public interface API {

    @GET("/station/list")
    public void getStationList(Callback<StationResponse> callback);

    @GET("/station/list/category/{category}")
    public void getStationList(@Path("category")String category, Callback<StationResponse> callback);

    @GET("/nowplaying")
    public void getNowPlaying(Callback<StationMetaResponse> callback);

    @GET("/show/latest")
    public void getShows(Callback<ShowResponse> callback);

    @GET("/show/index")
    public void getAllShows(Callback<ShowResponse> callback);

    @GET("/show/index/id/{id}")
    public void getEpisodesForShow(@Path("id")String id, Callback<ShowResponse> callback);

    /**
     * <p>A builder class for {@link com.ponyvillelive.app.net.API}. A default builder will use
     * http://ponyvillelive.com/api as the API host</p>
     */
    public static class Builder {
        private String hostUrl;

        public Builder setHostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public API build() {
            if(hostUrl == null) hostUrl = "http://ponyvillelive.com/api";

            return new RestAdapter.Builder()
                    .setEndpoint(hostUrl)
                    .setConverter(new GsonConverter(new Gson()))
                    .build()
                    .create(API.class);
        }
    }

}
