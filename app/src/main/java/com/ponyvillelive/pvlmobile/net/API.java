package com.ponyvillelive.pvlmobile.net;

import com.google.gson.Gson;
import com.ponyvillelive.pvlmobile.model.net.ArrayResponse;
import com.ponyvillelive.pvlmobile.model.net.MapResponse;
import com.ponyvillelive.pvlmobile.model.NowPlayingMeta;
import com.ponyvillelive.pvlmobile.model.net.ObjectResponse;
import com.ponyvillelive.pvlmobile.model.Show;
import com.ponyvillelive.pvlmobile.model.Station;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * <p>The code contract for the Ponyville Live! API as documented on
 * <a href="http://docs.ponyvillelive.apiary.io/">Apiary</a></p>
 */
public interface API {

    @GET("/nowplaying")
    public Observable<MapResponse<String, NowPlayingMeta>> getNowPlaying();

    @GET("/nowplaying/index/id/{id}")
    public Observable<ObjectResponse<NowPlayingMeta>> getNowPlayingForStation(@Path("id")int id);

    @GET("/station/list")
    public Observable<ArrayResponse<Station>> getStationList();

    @GET("/station/list/category/{category}")
    public Observable<ArrayResponse<Station>> getStationList(@Path("category")String category);

    @GET("/show/latest")
    public Observable<ArrayResponse<Show>> getShows();

    @GET("/show/index")
    public Observable<ArrayResponse<Show>> getAllShows();

    @GET("/show/index/id/{id}")
    public Observable<ObjectResponse<Show>> getEpisodesForShow(@Path("id")String id);

    /**
     * <p>A builder class for {@link com.ponyvillelive.pvlmobile.net.API}. A default builder will use
     * http://ponyvillelive.com/api as the API host</p>
     */
    public static class Builder {
        private String hostUrl;
        private Client client;
        private Converter converter;
        private RestAdapter.LogLevel logLevel;

        public Builder setHostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public Builder setClient(Client client) {
            this.client = client;
            return this;
        }

        public Builder setConverter(Converter converter) {
            this.converter = converter;
            return this;
        }

        public Builder setLogLevel(RestAdapter.LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public API build() {
            if(hostUrl == null) hostUrl = "http://ponyvillelive.com/api";
            if(client == null) client = new OkClient();
            if(converter == null) converter = new GsonConverter(new Gson());
            if(logLevel == null) logLevel = RestAdapter.LogLevel.NONE;

            return new RestAdapter.Builder()
                    .setEndpoint(hostUrl)
                    .setClient(client)
                    .setConverter(converter)
                    .setLogLevel(logLevel)
                    .build()
                    .create(API.class);
        }
    }

}
