package com.ponyvillelive.pvlmobile.net;

import android.app.Application;

import com.ponyvillelive.pvlmobile.BuildConfig;
import com.ponyvillelive.pvlmobile.ui.BottomDrawerFragment;
import com.ponyvillelive.pvlmobile.ui.StationAdapter;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import timber.log.Timber;

/**
 * This module provides all the network-related injections
 */
@Module(
        injects = {
                StationAdapter.class,
                BottomDrawerFragment.class
        },
        complete = false,
        library = true
)
public class NetModule {
    public static final String PRODUCTION_API_URL = "https://ponyvillelive.com/api";
    public static final int CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    @Provides
    @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(PRODUCTION_API_URL);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app) {
        return createOkHttpClient(app);
    }

    @Provides
    @Singleton
    Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, Client client) {
        return new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(endpoint)
                .setRequestInterceptor((requestFacade) ->
                        requestFacade.addHeader("User-Agent", BuildConfig.PACKAGE_NAME + "/" + BuildConfig.VERSION_NAME))
                .build();
    }

    @Provides
    @Singleton
    API provideAPI(RestAdapter restAdapter) {
        return restAdapter.create(API.class);
    }

    @Provides
    @Singleton
    Picasso providePicasso(Application app, OkHttpClient client) {
        Picasso.Builder builder = new Picasso.Builder(app);
        builder.downloader(new OkHttpDownloader(client));
        return builder.build();
    }

    public static OkHttpClient createOkHttpClient(Application app) {
        OkHttpClient client = new OkHttpClient();

        // Install an HTTP cache in the application cache directory.
        try {
            File cacheDir = new File(app.getCacheDir(), "http");
            Cache cache = new Cache(cacheDir, CACHE_SIZE);
            client.setCache(cache);
        } catch (IOException e) {
            Timber.e(e, "Unable to install disk cache.");
        }

        return client;
    }
}
