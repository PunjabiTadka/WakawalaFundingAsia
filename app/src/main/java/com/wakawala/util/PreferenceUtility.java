package com.wakawala.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by aayushi on 16/8/17.
 */

public class PreferenceUtility {

    public static final String HAS_LOGGED_IN = "HAS_LOGGED_IN";
    private static PreferenceUtility mPreferenceUtility;
    private final SharedPreferences mSharedPreferences;

    private PreferenceUtility(Context mContext) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static PreferenceUtility getInstance(Context mContext) {
        if (mPreferenceUtility == null)
            mPreferenceUtility = new PreferenceUtility(mContext.getApplicationContext());
        return mPreferenceUtility;
    }

    public void loggedOut() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear().apply();
    }

    public void setBooleanData(String name, boolean value) {
        mSharedPreferences.edit().putBoolean(name, value).apply();
    }

    public boolean getBooleanData(String name) {
        return mSharedPreferences.getBoolean(name, false);
    }

    public void saveUserDetail(String name, String value) {
        mSharedPreferences.edit().putString(name, value).apply();
    }

    public String getUserDetails(String s) {
        return mSharedPreferences.getString(s, null);
    }

}
