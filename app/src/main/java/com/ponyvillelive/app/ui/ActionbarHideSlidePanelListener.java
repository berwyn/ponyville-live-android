package com.ponyvillelive.app.ui;

import android.app.Activity;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * An implementation of {@link com.sothree.slidinguppanel.SlidingUpPanelLayout.SimplePanelSlideListener}
 * that hides the {@link android.app.ActionBar} when the panel is slid open, and reveals it again
 * when the panel is slid closed or hidden
 */
public class ActionbarHideSlidePanelListener extends SlidingUpPanelLayout.SimplePanelSlideListener {

    private Activity activity;

    public ActionbarHideSlidePanelListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onPanelCollapsed(View view) {
        if(activity.getActionBar() != null) {
            activity.getActionBar().show();
        }
    }

    @Override
    public void onPanelExpanded(View view) {
        if(activity.getActionBar() != null) {
            activity.getActionBar().hide();
        }
    }

    @Override
    public void onPanelHidden(View panel) {
        super.onPanelHidden(panel);
        if(!activity.getActionBar().isShowing()) {
            activity.getActionBar().show();
        }
    }
}
