package com.wakawala.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.wakawala.ApplicationLoader;

public class SessionManager {

    public static void setSessionString(String key, String value) {
        SharedPreferences.Editor editor = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getSessionString(String key, String defaultValue) {
        SharedPreferences sharedPreferences = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void setSessionInt(String key, int value) {
        SharedPreferences.Editor editor = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getSessionInt(String key, int defaultValue) {
        SharedPreferences sharedPreferences = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void setSessionBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getSessionBoolean(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
