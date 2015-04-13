package com.ponyvillelive.pvlmobile.net;

import android.app.Application;
import android.content.SharedPreferences;

import com.ponyvillelive.pvlmobile.prefs.ApiEndpoint;
import com.ponyvillelive.pvlmobile.prefs.ApiEndpoints;
import com.ponyvillelive.pvlmobile.prefs.IsMockMode;
import com.ponyvillelive.pvlmobile.prefs.MockDownloader;
import com.ponyvillelive.pvlmobile.prefs.NetworkProxy;
import com.ponyvillelive.pvlmobile.prefs.StringPreference;
import com.ponyvillelive.pvlmobile.ui.DebugAppContainer;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import retrofit.android.AndroidMockValuePersistence;

/**
 * Extends {@link com.ponyvillelive.pvlmobile.net.NetModule} to provide extra debug features
 */
@Module(
        addsTo = NetModule.class,
        injects = {
                DebugAppContainer.class
        },
        overrides = true,
        complete = false,
        library = true
)
public class DebugNetModule {

    @Provides
    @Singleton
    Picasso providePicasso(OkHttpClient client, MockRestAdapter mockRestAdapter,
                           @IsMockMode boolean isMockMode, Application app) {
        Picasso.Builder builder = new Picasso.Builder(app);
        if (isMockMode) {
            builder.downloader(new MockDownloader(mockRestAdapter, app.getAssets()));
        } else {
            builder.downloader(new OkHttpDownloader(client));
        }
        return builder.build();
    }

    @Provides
    @Singleton
    Endpoint provideEndpoint(@ApiEndpoint StringPreference apiEndpoint) {
        return Endpoints.newFixedEndpoint(apiEndpoint.get());
    }

    @Provides
    @Singleton
    MockRestAdapter provideMockRestAdapter(RestAdapter restAdapter, SharedPreferences preferences) {
        MockRestAdapter mockRestAdapter = MockRestAdapter.from(restAdapter);
        AndroidMockValuePersistence.install(mockRestAdapter, preferences);
        return mockRestAdapter;
    }

    @Provides
    @Singleton
    API provideMockApi(RestAdapter restAdapter, MockRestAdapter mockRestAdapter,
                       @IsMockMode boolean isMockMode, MockAPI mockService) {
        if(isMockMode) {
            return mockRestAdapter.create(API.class, mockService);
        }
        return restAdapter.create(API.class);
    }

    @Provides
    @Singleton
    @NetworkProxy
    StringPreference provideNetworkProxy(SharedPreferences preferences) {
        return new StringPreference(preferences, "debug_network_proxy");
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app) {
        OkHttpClient client = NetModule.createOkHttpClient(app);
        client.setSslSocketFactory(createBadSslSocketFactory());
        return client;
    }

    @Provides
    @Singleton
    @ApiEndpoint
    StringPreference provideEndpointPreference(SharedPreferences preferences) {
        return new StringPreference(preferences, "debug_endpoint", ApiEndpoints.MOCK_MODE.url);
    }

    @Provides
    @Singleton
    @IsMockMode
    boolean provideIsMockMode(@ApiEndpoint StringPreference endpoint) {
        return ApiEndpoints.isMockMode(endpoint.get());
    }

    private static SSLSocketFactory createBadSslSocketFactory() {
        try {
            // Construct SSLSocketFactory that accepts any cert.
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager permissive = new X509TrustManager() {
                @Override public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            context.init(null, new TrustManager[] { permissive }, null);
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
