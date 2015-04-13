package com.ponyvillelive.pvlmobile.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ponyvillelive.pvlmobile.R;
import com.ponyvillelive.pvlmobile.service.MusicService;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by berwyn on 31/03/15.
 */
public class FullScreenPlayerActivity extends ActionBarCastActivity {

    private static final String TAG = FullScreenPlayerActivity.class.getSimpleName();

    ImageView   thumbsUp;
    ImageView   thumbsDown;
    ImageView   playPause;
    TextView    line1;
    TextView    line2;
    TextView    line3;
    ImageView   artwork;
    ProgressBar loadingProgress;

    private Drawable            playDrawable;
    private Drawable            stopDrawable;
    private PlaybackStateCompat lastPlaybackState;

    private MediaControllerCompat.Callback callback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            Timber.d(TAG, "onPlaybackStateChanged", state);
            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata != null) {
                updateMediaDescription(metadata.getDescription());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_player);
        initializeToolbar();

        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        playDrawable = getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp);
        stopDrawable = getResources().getDrawable(R.drawable.ic_pause_white_48dp);

        if (savedInstanceState == null) {
            updateFromParams(getIntent());
        }
    }

    private void updateFromParams(Intent intent) {
        if (intent != null) {
            MediaDescriptionCompat description =
                    intent.getParcelableExtra(MediaPlayerActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION);
            if (description != null) {
                updateMediaDescription(description);
            }
        }
    }

    private void updateMediaDescription(MediaDescriptionCompat description) {
        if (description == null) {
            return;
        }
        Timber.d(TAG, "updateMediaDescription called");
        line1.setText(description.getTitle());
        line2.setText(description.getSubtitle());
        Picasso.with(this)
               .load(description.getIconUri())
               .centerInside()
               .into(artwork);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getMediaControllerCompat() != null) {
            getMediaControllerCompat().unregisterCallback(callback);
        }
    }

    @OnClick(R.id.play_pause)
    public void playPausePressed(View v) {
        PlaybackStateCompat state = getMediaControllerCompat().getPlaybackState();
        MediaControllerCompat.TransportControls controls =
                getMediaControllerCompat().getTransportControls();
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
            case PlaybackStateCompat.STATE_BUFFERING:
                controls.stop();
                break;
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_STOPPED:
                controls.play();
                break;
            default:
                Timber.d(TAG, "onClick with state ", state.getState());
        }
    }

    private void connectToSession(MediaSessionCompat.Token token) {
        MediaControllerCompat controller = null;
        try {
            controller = new MediaControllerCompat(FullScreenPlayerActivity.this, token);
        } catch (RemoteException e) {
            Timber.e(TAG, "connectToSession", e);
            finish();
            return;
        }

        if (controller.getMetadata() == null) {
            finish();
            return;
        }
        setMediaControllerCompat(controller);
        controller.registerCallback(callback);
        PlaybackStateCompat state = controller.getPlaybackState();
        updatePlaybackState(state);
        MediaMetadataCompat metadata = controller.getMetadata();
        if (metadata != null) {
            updateMediaDescription(metadata.getDescription());
        }
    }

    private void updatePlaybackState(PlaybackStateCompat state) {
        if (state == null) {
            return;
        }

        lastPlaybackState = state;
        String castName = getMediaControllerCompat()
                .getExtras().getString(MusicService.EXTRA_CONNECTED_CAST);
        String line3Text = "";
        if (castName != null) {
            line3Text = getResources().getString(R.string.casting_to_device, castName);
        }
        line3.setText(line3Text);

        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                loadingProgress.setVisibility(View.INVISIBLE);
                playPause.setVisibility(View.VISIBLE);
                playPause.setImageDrawable(stopDrawable);
                break;
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                loadingProgress.setVisibility(View.INVISIBLE);
                playPause.setVisibility(View.VISIBLE);
                playPause.setImageDrawable(playDrawable);
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                loadingProgress.setVisibility(View.VISIBLE);
                playPause.setVisibility(View.INVISIBLE);
                playPause.setImageDrawable(playDrawable);
                line3.setText(R.string.loading);
                break;
            default:
                Timber.d(TAG, "updatePlaybackState called with unhandled state", state.getState());
        }
    }
}
