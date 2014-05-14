package com.ponyvillelive.app.net;

/**
 * <p>Singleton manager for an instance of {@link com.ponyvillelive.app.net.API}. Allows sharing and
 * reuse of a single instance.</p>
 */
public final class APIProvider {

    private static API instance;

    public static API getInstance() {
        if(instance == null)
            instance = new API.Builder().build();

        return instance;
    }

}
