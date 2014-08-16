package com.ponyvillelive.app;

/**
 * Created by berwyn on 16/08/14.
 */
public final class Modules {
    public static Object[] list(PvlApp app) {
        return new Object[] {
                new PvlModule(app)
        };
    }
}
