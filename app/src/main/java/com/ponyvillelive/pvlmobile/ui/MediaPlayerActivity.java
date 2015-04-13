package com.ponyvillelive.pvlmobile.ui;

import com.ponyvillelive.pvlmobile.BuildConfig;

/**
 * Created by berwyn on 09/04/15.
 */
public class MediaPlayerActivity extends BaseActivity {

    public static final String EXTRA_PLAY_QUERY       = BuildConfig.PACKAGE_NAME + ".PLAY_QUERY";
    public static final String EXTRA_START_FULLSCREEN =
            BuildConfig.PACKAGE_NAME + ".EXTRA_START_FULLSCREEN";

    // Optionally used with @{link EXTRA_START_FULLSCREEN} to carry a session token,
    // speeding up the mediacontroller connection.
    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION =
            BuildConfig.PACKAGE_NAME + ".CURRENT_MEDIA_DESCRIPTION";

    private static final String TAG            = MediaPlayerActivity.class.getSimpleName();
    private static final String SAVED_MEDIA_ID = BuildConfig.PACKAGE_NAME + ".MEDIA_ID";

    @Override
    protected void onMediaControllerConnected() {

    }
}
