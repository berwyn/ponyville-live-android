package com.ponyvillelive.app.ui;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berwyn on 16/08/14.
 */
@Module(
        injects = {
                DebugAppContainer.class
        },
        complete = false,
        library = true,
        overrides = true
)
public class DebugUiModule {
    @Provides
    @Singleton
    AppContainer provideAppContainer(DebugAppContainer appContainer) {
        return appContainer;
    }
}
