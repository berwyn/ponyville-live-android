package com.ponyvillelive.pvlmobile;

import com.ponyvillelive.pvlmobile.net.DebugNetModule;
import com.ponyvillelive.pvlmobile.ui.DebugUiModule;

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
