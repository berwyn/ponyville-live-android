package com.ponyvillelive.app;

import com.ponyvillelive.app.net.DebugNetModule;
import com.ponyvillelive.app.ui.DebugUiModule;

import dagger.Module;

/**
 * Created by berwyn on 16/08/14.
 */
@Module(
        addsTo = PvlModule.class,
        includes = {
                DebugUiModule.class,
                DebugNetModule.class
        },
        overrides = true
)
public class DebugPvlModule {
}
