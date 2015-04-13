package com.ponyvillelive.pvlmobile.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ponyvillelive.pvlmobile.R;
import com.ponyvillelive.pvlmobile.service.MusicService;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by berwyn on 29/03/15.
 */
public class PlaybackControlsFragment extends Fragment {

    private static String TAG = PlaybackControlsFragment.class.getSimpleName();

    @InjectView(R.id.play_pause)
    ImageButton playPause;
    @InjectView(R.id.title)
    TextView    title;
    @InjectView(R.id.artist)
    TextView    subtitle;
    @InjectView(R.id.extra_info)
    TextView    extraInfo;
    @InjectView(R.id.album_art)
    ImageView   albumArt;
    String      artUrl;

    private MediaControllerCompat.Callback callback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            if (state == null) {
                return;
            }
            Timber.d(TAG, "Received playback state change to state ", state.getState());
            PlaybackControlsFragment.this.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata == null) {
                return;
            }
            Timber.d(TAG, "Received metadata state change to mediaId=",
                    metadata.getDescription().getMediaId(),
                    " song=", metadata.getDescription().getTitle());
            PlaybackControlsFragment.this.onMetadataChanged(metadata);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);

        ButterKnife.inject(this, rootView);

        playPause.setEnabled(true);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            playPause.getDrawable().setTint(R.color.colorAccent);
            playPause.getDrawable().setTintMode(PorterDuff.Mode.SRC_IN);
        }

        rootView.setOnClickListener((v) -> {
            Intent intent = new Intent(getActivity(), FullScreenPlayerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            MediaMetadataCompat metadata = ((BaseActivity) getActivity()).getMediaControllerCompat().getMetadata();
            if (metadata != null) {
                intent.putExtra(MediaPlayerActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION,
                        metadata.getDescription());
            }
            startActivity(intent);
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d(TAG, "fragment.onStart");
        if (((BaseActivity) getActivity()).getMediaControllerCompat() != null) {
            onConnected();
        }
    }

    public void onConnected() {
        MediaControllerCompat mediaController = ((BaseActivity) getActivity()).getMediaControllerCompat();
        Timber.d(TAG, "onConnected, mediaController==null?", mediaController == null);
        if (mediaController != null) {
            onMetadataChanged(mediaController.getMetadata());
            onPlaybackStateChanged(mediaController.getPlaybackState());
            mediaController.registerCallback(callback);
        }
    }

    private void onMetadataChanged(MediaMetadataCompat metadata) {
        Timber.d(TAG, "onMetadataChanged ", metadata);
        if (getActivity() == null) {
            Timber.d(TAG, "onMetadataChanged called when getActivity null," +
                    "this should not happen if the callback was properly unregistered. Ignoring.");
            return;
        }
        if (metadata == null) {
            return;
        }

        title.setText(metadata.getDescription().getTitle());
        subtitle.setText(metadata.getDescription().getSubtitle());
        String artUrl = metadata.getDescription().getIconUri().toString();
        if (!TextUtils.equals(artUrl, this.artUrl)) {
            this.artUrl = artUrl;
            Picasso.with(getActivity())
                   .load(artUrl)
                   .centerCrop()
                   .into(albumArt);
        }
    }

    private void onPlaybackStateChanged(PlaybackStateCompat state) {
        Timber.d(TAG, "onPlaybackStateChanged ", state);
        if (getActivity() == null) {
            Timber.d(TAG, "onPlaybackStateChanged called when getActivity null," +
                    "this should not happen if the callback was properly unregistered. Ignoring.");
            return;
        }
        if (state == null) {
            return;
        }
        boolean enablePlay = false;
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_STOPPED:
                    enablePlay = true;
                break;
            case PlaybackStateCompat.STATE_ERROR:
                Timber.d(TAG, "error playbackstate: ", state.getErrorMessage());
                break;
        }

        if (enablePlay) {
            playPause.setImageDrawable(
                    getActivity().getDrawable(R.drawable.ic_play_arrow_black_36dp));
        } else {
            playPause.setImageDrawable(
                    getActivity().getDrawable(R.drawable.ic_pause_black_36dp));
        }

        MediaControllerCompat controller = ((BaseActivity) getActivity()).getMediaControllerCompat();
        String extraInfo = null;
        if (controller != null && controller.getExtras() != null) {
            String castName = controller.getExtras().getString(MusicService.EXTRA_CONNECTED_CAST);
            if (castName != null) {
                extraInfo = getResources().getString(R.string.casting_to_device, castName);
            }
        }
        setExtraInfo(extraInfo);
    }

    public void setExtraInfo(String extraInfo) {
        if (extraInfo == null) {
            this.extraInfo.setVisibility(View.GONE);
        } else {
            this.extraInfo.setText(extraInfo);
            this.extraInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d(TAG, "fragment.onStop");
        if (((BaseActivity) getActivity()).getMediaControllerCompat() != null) {
            ((BaseActivity) getActivity()).getMediaControllerCompat().unregisterCallback(callback);
        }
    }

    @OnClick(R.id.play_pause)
    void playPauseClickListener(View v) {
        PlaybackStateCompat stateObj = ((BaseActivity) getActivity())
                .getMediaControllerCompat().getPlaybackState();
        final int state = stateObj == null
                ? PlaybackStateCompat.STATE_NONE
                : stateObj.getState();

        Timber.d(TAG, "Button pressed, in state " + state);
        switch (v.getId()) {
            case R.id.play_pause:
                if (state == PlaybackStateCompat.STATE_PAUSED
                        || state == PlaybackStateCompat.STATE_STOPPED
                        || state == PlaybackStateCompat.STATE_NONE) {
                    playMedia();
                } else if (state == PlaybackStateCompat.STATE_PLAYING
                        || state == PlaybackStateCompat.STATE_BUFFERING
                        || state == PlaybackStateCompat.STATE_CONNECTING) {
                    pauseMedia();
                }
                break;
        }
    }

    private void playMedia() {
        MediaControllerCompat controller = ((BaseActivity) getActivity()).getMediaControllerCompat();
        if (controller != null) {
            controller.getTransportControls().play();
        }
    }

    private void pauseMedia() {
        MediaControllerCompat controller = ((BaseActivity) getActivity()).getMediaControllerCompat();
        if (controller != null) {
            controller.getTransportControls().pause();
        }
    }
}
