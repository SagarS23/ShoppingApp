package com.seasapps.shoppingapp;

import android.app.Application;
import android.content.Context;

import com.seasapps.shoppingapp.Session.Preferences;

public class ShoppingApp extends Application {

    private static Context context;
    private static ShoppingApp instance;
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        doInit();
        instance = this;
    }

    public static Context getContext() {
        return context;
    }

    private void doInit() {
        context = getApplicationContext();
        this.preferences = new Preferences(this);
    }

    public static ShoppingApp getInstance() {
        return instance;
    }

    public static void setInstance(ShoppingApp instance) {
        ShoppingApp.instance = instance;
    }

    public synchronized Preferences getPreferences() {
        return preferences;
    }
}
