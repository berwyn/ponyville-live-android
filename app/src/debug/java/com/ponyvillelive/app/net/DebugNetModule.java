package com.ponyvillelive.app.net;

import android.app.Application;
import android.content.SharedPreferences;

import com.ponyvillelive.app.prefs.ApiEndpoint;
import com.ponyvillelive.app.prefs.ApiEndpoints;
import com.ponyvillelive.app.prefs.IsMockMode;
import com.ponyvillelive.app.prefs.MockDownloader;
import com.ponyvillelive.app.prefs.NetworkProxy;
import com.ponyvillelive.app.prefs.StringPreference;
import com.ponyvillelive.app.ui.DebugAppContainer;
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
 * Created by berwyn on 16/08/14.
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
