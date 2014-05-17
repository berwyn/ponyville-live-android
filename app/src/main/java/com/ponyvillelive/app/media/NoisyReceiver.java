package com.ponyvillelive.app.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.ponyvillelive.app.BusProvider;
import com.ponyvillelive.app.event.StopRequestedEvent;

public class NoisyReceiver extends BroadcastReceiver {
    public NoisyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            // signal your service to stop playback
            // (via an Intent, for instance)
            BusProvider.getBus().post(new StopRequestedEvent());
        }
    }
}
