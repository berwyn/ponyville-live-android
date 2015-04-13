package com.ponyvillelive.pvlmobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ponyvillelive.pvlmobile.BuildConfig;

/**
 * Created by berwyn on 09/04/15.
 */
public class MusicService extends Service {

    // Extra on MediaSession that contains the Cast device name currently connected to
    public static final String EXTRA_CONNECTED_CAST = BuildConfig.PACKAGE_NAME + ".CAST_NAME";
    // The action of the incoming Intent indicating that it contains a command
    // to be executed (see {@link #onStartCommand})
    public static final String ACTION_CMD = BuildConfig.PACKAGE_NAME + ".ACTION_CMD";
    // The key in the extras of the incoming Intent indicating the command that
    // should be executed (see {@link #onStartCommand})
    public static final String CMD_NAME = "CMD_NAME";
    // A value of a CMD_NAME key in the extras of the incoming Intent that
    // indicates that the music playback should be paused (see {@link #onStartCommand})
    public static final String CMD_PAUSE = "CMD_PAUSE";
    // A value of a CMD_NAME key that indicates that the music playback should switch
    // to local playback from cast playback.
    public static final String CMD_STOP_CASTING = "CMD_STOP_CASTING";

    private static final String TAG = MusicService.class.getSimpleName();
    // Action to thumbs up a media item
    private static final String CUSTOM_ACTION_THUMBS_UP = BuildConfig.PACKAGE_NAME + ".THUMBS_UP";
    // Delay stopSelf by using a handler.
    private static final int STOP_DELAY = 30000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
