package com.ponyvillelive.app.ui;

import android.app.Activity;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by berwyn on 21/08/14.
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
