package com.actiknow.clearsale.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class BuyerDetailsPref {
    public static String BUYER_NAME = "buyer_name";
    public static String BUYER_EMAIL = "buyer_email";
    public static String BUYER_MOBILE = "buyer_mobile";
    public static String BUYER_LOGIN_KEY = "buyer_login_key";
    public static String BUYER_ID = "buyer_id";
    public static String BUYER_FIREBASE_ID = "buyer_firebase_id";
    public static String BUYER_IMAGE = "buyer_image";
    public static String BUYER_FACEBOOK_ID = "buyer_facebook_id";
    public static String BUYER_LINKEDIN_ID = "buyer_linkedin_id";
    public static String PROFILE_STATUS = "profile_status";
    public static String PROFILE_HOME_TYPE = "profile_home_type";
    public static String PROFILE_STATE = "profile_state";
    public static String PROFILE_PRICE_RANGE = "profile_price_range";
    private static BuyerDetailsPref buyerDetailsPref;
    private String BUYER_DETAILS = "BUYER_DETAILS";
    
    public static BuyerDetailsPref getInstance () {
        if (buyerDetailsPref == null)
            buyerDetailsPref = new BuyerDetailsPref ();
        return buyerDetailsPref;
    }

    private SharedPreferences getPref (Context context) {
        return context.getSharedPreferences (BUYER_DETAILS, Context.MODE_PRIVATE);
    }

    public String getStringPref (Context context, String key) {
        return getPref (context).getString (key, "");
    }

    public int getIntPref (Context context, String key) {
        return getPref (context).getInt (key, 0);
    }

    public boolean getBooleanPref (Context context, String key) {
        return getPref (context).getBoolean (key, false);
    }

    public void putBooleanPref (Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putBoolean (key, value);
        editor.apply ();
    }

    public void putStringPref (Context context, String key, String value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putString (key, value);
        editor.apply ();
    }

    public void putIntPref (Context context, String key, int value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putInt (key, value);
        editor.apply ();
    }
}
