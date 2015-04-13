package com.ponyvillelive.pvlmobile.ui;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by berwyn on 19/03/15.
 */
public class DrawerMenuContents {
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_ICON  = "icon";

    private List<Map<String, ?>> items;
    private Class[]              activities;

    public DrawerMenuContents(Context ctx) {
        activities = new Class[0];
        items = new ArrayList<>(0);
    }

    public List<Map<String, ?>> getItems() {
        return items;
    }

    public Class getActivity(int position) {
        return activities[position];
    }

    public int getPosition(Class activityClass) {
        for(int i = 0; i < activities.length; i++) {
            if(activities[i].equals(activityClass)) {
                return i;
            }
        }
        return -1;
    }

    private Map<String, ?> populateDrawerItem(String title, int icon) {
        Map<String, Object> item = new HashMap<>();
        item.put(FIELD_TITLE, title);
        item.put(FIELD_ICON, icon);
        return item;
    }
}
