package com.ponyvillelive.pvlmobile;

import android.app.Application;
import android.content.Context;

import com.ponyvillelive.pvlmobile.ui.ActivityHierarchyServer;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Created by tyr on 22/05/2014.
 */
public class PvlApp extends Application {
    private ObjectGraph objectGraph;

    @Inject
    ActivityHierarchyServer activityHierarchyServer;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // TODO Crashlytics.start(this);
            // TODO Timber.plant(new CrashlyticsTree());
        }

        buildObjectGraphAndInject();

        registerActivityLifecycleCallbacks(activityHierarchyServer);
    }

    public void buildObjectGraphAndInject() {
        long start = System.nanoTime();

        objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);

        long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        Timber.i("Global object graph creation took %sms", diff);
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static PvlApp get(Context context) {
        return (PvlApp) context.getApplicationContext();
    }
}
