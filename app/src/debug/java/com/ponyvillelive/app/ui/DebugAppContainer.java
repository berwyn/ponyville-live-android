package com.ponyvillelive.app.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jakewharton.madge.MadgeFrameLayout;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.ponyvillelive.app.PvlApp;
import com.ponyvillelive.app.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by berwyn on 16/08/14.
 */
@Singleton
public class DebugAppContainer implements AppContainer {

    private PvlApp   app;
    private Activity activity;
    private Context  drawerContext;

    @InjectView(R.id.debug_drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.debug_content)
    ViewGroup    content;

    @InjectView(R.id.madge_container)
    MadgeFrameLayout   madgeFrameLayout;
    @InjectView(R.id.debug_content)
    ScalpelFrameLayout scalpelFrameLayout;

    @Inject
    public DebugAppContainer() {
    }

    @Override
    public ViewGroup get(Activity activity, PvlApp app) {
        this.app = app;
        this.activity = activity;
        this.drawerContext = activity;

        activity.setContentView(R.layout.debug_activity_frame);

        ViewGroup drawer = (ViewGroup) activity.findViewById(R.id.debug_drawer);
        LayoutInflater.from(drawerContext).inflate(R.layout.debug_drawer_content, drawer);

        ButterKnife.inject(this, activity);

        return content;
    }
}
