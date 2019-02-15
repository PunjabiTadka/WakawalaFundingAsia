package com.wakawala.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;
import com.wakawala.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Util {

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

            return isConnected;
        } else {
            return false;
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void saveLocally(Context context, String key, String value) {

        context.getSharedPreferences("wakawala", Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static String getLocalValue(Context context, String key) {

        return context.getSharedPreferences("wakawala", Context.MODE_PRIVATE).getString(key, null);
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static float convertDpToPixels(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertSpToPixels(Context context, float sp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = sp * ((float) metrics.scaledDensity / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static SparseIntArray getSparseArrayOfMainTabDrawables() {

        SparseIntArray sparseIntArray = new SparseIntArray();
        sparseIntArray.put(Constants.CAMPAIGN_LIST, R.drawable.bottom_campaign_list_selector);
        sparseIntArray.put(Constants.SEARCH, R.drawable.bottom_search_selector);
        sparseIntArray.put(Constants.ADD_CAMPAIGN, R.drawable.bottom_add_campaign_selector);
        sparseIntArray.put(Constants.FAVORITE, R.drawable.bottom_favorite_selector);
        sparseIntArray.put(Constants.PROFILE, R.drawable.bottom_profile_selector);

        return sparseIntArray;
    }

    public static SparseIntArray getSparseArrayOfCampaignTabStrRes() {

        SparseIntArray sparseIntArray = new SparseIntArray();
        sparseIntArray.put(Constants.POPULAR, R.string.popular);
        sparseIntArray.put(Constants.RECENT, R.string.recent);

        return sparseIntArray;
    }


    public static SparseIntArray getSparseArrayOfSearchTabStrRes() {

        SparseIntArray sparseIntArray = new SparseIntArray();
        sparseIntArray.put(Constants.CAMPAIGN_ID, R.string.campaign_id);
        sparseIntArray.put(Constants.CAMPAIGN_NAME, R.string.campaign_name);
        sparseIntArray.put(Constants.USERNAME, R.string.username);

        return sparseIntArray;
    }

    public interface Constants {
        int CAMPAIGN_LIST = 0;
        int SEARCH = 1;
        int ADD_CAMPAIGN = 2;
        int FAVORITE = 3;
        int PROFILE = 4;

        int POPULAR = 0;
        int RECENT = 1;

        int CAMPAIGN_ID = 0;
        int CAMPAIGN_NAME = 1;
        int USERNAME = 2;


    }

    public static void showSnackbar(View view, String string) {

        Snackbar.make(view, string, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceToken(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
