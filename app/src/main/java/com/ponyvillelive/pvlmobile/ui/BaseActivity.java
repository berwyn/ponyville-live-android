package com.ponyvillelive.pvlmobile.ui;

import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.ponyvillelive.pvlmobile.R;
import com.ponyvillelive.pvlmobile.util.NetworkHelper;

import timber.log.Timber;

/**
 * Created by berwyn on 23/03/15.
 */
public abstract class BaseActivity extends ActionBarCastActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    private PlaybackControlsFragment controlsFragment;
    private final MediaControllerCompat.Callback mediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        Timber.d(TAG, "mediaControllerCallback.onPlaybackStateChanged: " +
                                        "hiding controls because state is ",
                                state == null ? "null" : state.getState());
                        hidePlaybackControls();
                    }
                }

                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        Timber.d(TAG, "mediaControllerCallback.onMetadataChanged: " +
                                "hiding controls because metadata is null");
                        hidePlaybackControls();
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.d(TAG, "Activity onCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d(TAG, "Activity onStart");

        controlsFragment = (PlaybackControlsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_playback_controls);
        if (controlsFragment == null) {
            throw new IllegalStateException("Missing fragment with id `controls`, cannot continue.");
        }

        hidePlaybackControls();
    }

    protected void hidePlaybackControls() {
        Timber.d(TAG, "hidePlaybackControls");
        getSupportFragmentManager().beginTransaction()
                                   .hide(controlsFragment)
                                   .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.d(TAG, "Activity onStop");


    }

    private void connectToMediaSession(MediaSessionCompat.Token token) {
        MediaControllerCompat mediaController;
        try {
            mediaController = new MediaControllerCompat(this, token);
        } catch (RemoteException e) {
            Timber.e(TAG, "connectToMediaSession failed", e);
            throw new RuntimeException(e);
        }
        mediaController.registerCallback(mediaControllerCallback);
        setMediaControllerCompat(mediaController);

        if (shouldShowControls()) {
            showPlaybackControls();
        } else {
            Timber.d(TAG, "connectionCallback.onConnected: " +
                    "hiding controls because metadata is null");
            hidePlaybackControls();
        }

        if (controlsFragment != null) {
            controlsFragment.onConnected();
        }

        onMediaControllerConnected();
    }

    protected boolean shouldShowControls() {
        MediaControllerCompat mediaController = getMediaControllerCompat();
        if (mediaController == null ||
                mediaController.getMetadata() == null ||
                mediaController.getPlaybackState() == null) {
            return false;
        }

        switch (mediaController.getPlaybackState().getState()) {
            case PlaybackState.STATE_ERROR:
            case PlaybackState.STATE_NONE:
            case PlaybackState.STATE_STOPPED:
                return false;
            default:
                return true;
        }
    }

    protected void showPlaybackControls() {
        Timber.d(TAG, "showPlaybackControls");
        if (NetworkHelper.isOnline(this)) {
            getSupportFragmentManager().beginTransaction()
                                       .setCustomAnimations(
                                               R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
                                               R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
                                       .show(controlsFragment)
                                       .commit();
        }
    }

    protected abstract void onMediaControllerConnected();

}
