package com.ponyvillelive.pvlmobile.ui;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berwyn on 16/08/14.
 */
@Module(
        injects = {
                MainActivity.class
        },
        complete = false,
        library = true
)
public class UiModule {
    @Provides @Singleton
    AppContainer provideAppContainer() {
        return AppContainer.DEFAULT;
    }

    @Provides @Singleton
    ActivityHierarchyServer provideActivityHierarchyServer() {
        return ActivityHierarchyServer.NONE;
    }
}
