package com.muhib.restaurant.utils;

import android.content.SharedPreferences;

import com.muhib.restaurant.MyApplication;

/**
 * Created by RR on 05-Apr-18.
 */

public class MySheardPreference {
    public static final String keyRestaurantPrefs = "restaurantPref";

    private static final String keyUserSiteUrl = "userSiteUrl";
    private static final String keyIsFirstTime = "isFirstTime";
    private static final String keyUserId = "userId";
    private static final String keyUserPassword = "userPassword";
    private static final String keyMood = "mood";

    private static SharedPreferences getSharedPreferences() {
        return MyApplication.getmInstance().getSharedPreferences(keyRestaurantPrefs, 0);
    }
    public static void setUsingFirstTime(boolean isFirstTime) {
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(keyIsFirstTime, isFirstTime);
        editor.apply();
    }

    public static boolean getUsingFirstTime() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getBoolean(keyIsFirstTime, true);
    }

    public static void setUserSiteUrl(String userSiteUrl) {
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();

        editor.putString(keyUserSiteUrl, userSiteUrl);
        editor.apply();
    }
    public static void setUserIdAndPassword(String userId, String password) {
        final SharedPreferences pref = getSharedPreferences();
        final SharedPreferences.Editor editor = pref.edit();

        editor.putString(keyUserId, userId);
        editor.putString(keyUserPassword, password);
        editor.apply();
    }

    public static String getUserSiteUrl() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getString(keyUserSiteUrl, "");
    }
    public static String getUserId() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getString(keyUserId, "");
    }
    public static String getUserPassword() {
        final SharedPreferences pref = getSharedPreferences();
        return pref.getString(keyUserPassword, "");
    }


}
