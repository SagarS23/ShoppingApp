package com.seasapps.shoppingapp.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preference class to store user session
 */

public class Preferences {

    private Context context;
    private static final String LOGGED_IN_USER = "LOGGED_IN_USER";
    private static final String LAT = "LAT";
    private static final String LNG = "LNG";

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Preferences(Context context) {
        this.context = context;
    }

    protected SharedPreferences getSharedPreferences(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getString(String key, String def) {
        SharedPreferences prefs = getSharedPreferences(key);
        String s = prefs.getString(key, def);
        return s;
    }

    public void setString(String key, String val) {
        SharedPreferences prefs = getSharedPreferences(key);
        SharedPreferences.Editor e = prefs.edit();
        e.putString(key, val);
        e.commit();
    }

    public void setLat(String lat) {
        setString(LAT, lat);
    }

    public String getLat() {
        return getString(LAT, "");
    }

    public void setLng(String lng) {
        setString(LNG, lng);
    }

    public String getLng() {
        return getString(LNG, "");
    }

    public void setLoggedInUser(String user) {
        setString(LOGGED_IN_USER, user);
    }

    public String getLoggedInUser() {
        return getString(LOGGED_IN_USER, "");
    }
}
