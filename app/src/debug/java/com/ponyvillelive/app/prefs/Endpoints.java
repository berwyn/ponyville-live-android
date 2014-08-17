package com.ponyvillelive.app.prefs;

import com.ponyvillelive.app.net.NetModule;

public enum Endpoints {
    PRODUCTION("Production", NetModule.PRODUCTION_API_URL),
    // STAGING("Staging", "https://api.staging.imgur.com/3/"),
    MOCK_MODE("Mock Mode", "mock://"),
    CUSTOM("Custom", null);

    public final String name;
    public final String url;

    Endpoints(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static Endpoints from(String endpoint) {
        for (Endpoints value : values()) {
            if (value.url != null && value.url.equals(endpoint)) {
                return value;
            }
        }
        return CUSTOM;
    }

    public static boolean isMockMode(String endpoint) {
        return from(endpoint) == MOCK_MODE;
    }
}
