package com.wakawala.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.wakawala.ApplicationLoader;

import androidx.appcompat.app.AppCompatActivity;

public class Utility {

    public static void callActivity(Context context, Intent myIntent) {
        context.startActivity(myIntent);
    }

    public static void callActivity(Context context, Class<?> classes) {
        Intent myIntent = new Intent(context, classes);
        callActivity(context, myIntent);
    }

    public static void callActivity(Context context, Class<?> classes, String key, String value) {
        Intent myIntent = new Intent(context, classes);
        myIntent.putExtra(key, value);
        callActivity(context, myIntent);
    }

    public static void callActivity(Context context, Class<?> classes, String key, int value) {
        Intent myIntent = new Intent(context, classes);
        myIntent.putExtra(key, value);
        callActivity(context, myIntent);
    }

    public static void callActivity(Context context, Class<?> classes, String key, String value, String key1, String value1) {
        Intent myIntent = new Intent(context, classes);
        myIntent.putExtra(key, value);
        myIntent.putExtra(key1, value1);
        callActivity(context, myIntent);
    }

    public static void callNewActivity(Context context, Class<?> classes) {
        Intent myIntent = new Intent(context, classes);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        callActivity(context, myIntent);
    }

    public static void killActivity(AppCompatActivity appCompatActivity) {
        if (appCompatActivity != null) {
            appCompatActivity.finish();
        }
    }

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ApplicationLoader.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) return false;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
