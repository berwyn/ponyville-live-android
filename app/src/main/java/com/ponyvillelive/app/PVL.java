package com.ponyvillelive.app;

import android.app.Application;

import com.ponyvillelive.app.model.Station;
import com.ponyvillelive.app.model.StationResponse;
import com.ponyvillelive.app.net.API;
import com.ponyvillelive.app.net.APIProvider;
import com.ponyvillelive.app.ui.StationAdapter;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by tyr on 22/05/2014.
 */
public class PVL extends Application {
    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        API service = APIProvider.getInstance();
        service.getStationList(Station.STATION_TYPE_AUDIO, new Callback<StationResponse>() {
            @Override
            public void success(StationResponse stationResponse, Response response) {
                Timber.d("Response came back!");
                DataCache.cacheStationList(Station.STATION_TYPE_AUDIO, stationResponse.result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Timber.d(retrofitError.getMessage());
            }
        });
        service.getStationList(Station.STATION_TYPE_VIDEO, new Callback<StationResponse>() {
            @Override
            public void success(StationResponse stationResponse, Response response) {
                Timber.d("Response came back!");
                DataCache.cacheStationList(Station.STATION_TYPE_VIDEO, stationResponse.result);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.HollowTree {
        @Override
        public void i(String message, Object... args) {
            // TODO e.g., Crashlytics.log(String.format(message, args));
        }

        @Override
        public void i(Throwable t, String message, Object... args) {
            i(message, args); // Just add to the log.
        }

        @Override
        public void e(String message, Object... args) {
            i("ERROR: " + message, args); // Just add to the log.
        }

        @Override
        public void e(Throwable t, String message, Object... args) {
            e(message, args);

            // TODO e.g., Crashlytics.logException(t);
        }
    }
}
