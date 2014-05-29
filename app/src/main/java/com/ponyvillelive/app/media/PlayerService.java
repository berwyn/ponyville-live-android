package com.ponyvillelive.app.media;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.ponyvillelive.app.BusProvider;
import com.ponyvillelive.app.event.PlayRequestedEvent;
import com.ponyvillelive.app.event.PlaybackStartedEvent;
import com.ponyvillelive.app.event.PlaybackStoppedEvent;
import com.ponyvillelive.app.event.StopRequestedEvent;
import com.ponyvillelive.app.model.Station;
import com.ponyvillelive.app.ui.MainActivity;
import com.ponyvillelive.app.ui.PlayingTrackNotification;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.io.IOException;

/**
 * An implementation of {@link android.app.Service} to handle media playback in its own process
 */
public class PlayerService extends Service implements AudioManager.OnAudioFocusChangeListener {

    private Bus eventBus;
    private Station currentStation;
    private MediaPlayer mediaPlayer;
    private WifiManager.WifiLock wifiLock;

    private static final int NOTIFICATION_ID = 1337;
    private static final String TAG = "MediaPlayer";

    public PlayerService() {
        eventBus = new Bus();
        eventBus.register(this);
    }

    @Override
    public void onCreate() {
        eventBus = BusProvider.getBus();
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
        }

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus((focusChange) -> {
            // TODO: Do we need anything here?
        });

        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) playRequested(new PlayRequestedEvent(currentStation));
                else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    @Subscribe
    public void playRequested(PlayRequestedEvent event) {
        if(event.station == null) return;
        currentStation = event.station;

        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "com.ponyvillelive.app");
        wifiLock.acquire();

        prepareMediaPlayer(event);
        
        Intent notificationIntent = new Intent(this, MainActivity.class);
        Notification foregroundNotification = PlayingTrackNotification.buildNotification(getApplicationContext(), "[DEBUG]");

        startForeground(NOTIFICATION_ID,foregroundNotification);
    }

    private void prepareMediaPlayer(PlayRequestedEvent event) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        try {
            mediaPlayer.setDataSource(event.station.streamUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener((mediaPlayer) -> {
            mediaPlayer.start();
            eventBus.post(producePlaybackStartedEvent());
        });

        mediaPlayer.setOnErrorListener((mediaPlayer, what, extra) -> {
            Log.d(TAG, "media player error");
            return false;
        });

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mediaPlayer.prepareAsync();
        }
    }

    @Subscribe
    public void stopRequested(StopRequestedEvent event) {
        if(wifiLock != null) {
            wifiLock.release();
            wifiLock = null;
        }
        if(mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            eventBus.post(new PlaybackStoppedEvent());
        }
        stopForeground(true);
    }

    @Produce
    public PlayRequestedEvent producePlayRequestEvent() {
        return new PlayRequestedEvent(currentStation);
    }

    @Produce
    public PlaybackStartedEvent producePlaybackStartedEvent() {
        return new PlaybackStartedEvent(currentStation);
    }
}
