package com.ponyvillelive.app.net;

/**
 * Created by tyr on 10/05/2014.
 */
public final class APIProvider {

    private static API instance;

    public static API getInstance() {
        if(instance == null)
            instance = new API.Builder().build();

        return instance;
    }

}
