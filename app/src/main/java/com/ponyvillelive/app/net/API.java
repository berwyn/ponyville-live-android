package com.ponyvillelive.app.net;

import com.google.gson.Gson;
import com.ponyvillelive.app.model.StationResponse;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by tyr on 10/05/2014.
 */
public interface API {

    @GET("/station/list")
    public void getStationList(Callback<StationResponse> callback);

    @GET("/station/list/category/{category}")
    public void getStationList(@Path("category")String category, Callback<StationResponse> callback);

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
