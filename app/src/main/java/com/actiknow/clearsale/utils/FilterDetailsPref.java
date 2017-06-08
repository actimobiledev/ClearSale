package com.actiknow.clearsale.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FilterDetailsPref {
    public static String FILTER_APPLIED = "filter_applied";
    public static String FILTER_LOCATION = "filter_location";
    public static String FILTER_PRICE_MIN = "filter_price_min";
    public static String FILTER_PRICE_MAX = "filter_price_max";
    public static String FILTER_BEDROOMS = "filter_bedrooms";
    public static String FILTER_BATHROOMS = "filter_bathrooms";
    public static String FILTER_STATUS = "filter_status";
    public static String FILTER_CITIES = "filter_cities";
    public static String FILTER_CITIES_JSON = "filter_cities_json";
    
    private static FilterDetailsPref filterDetailsPref;
    private String PROPERTY_DETAILS = "FILTER_DETAILS";
    
    public static FilterDetailsPref getInstance () {
        if (filterDetailsPref == null)
            filterDetailsPref = new FilterDetailsPref ();
        return filterDetailsPref;
    }
    
    private SharedPreferences getPref (Context context) {
        return context.getSharedPreferences (PROPERTY_DETAILS, Context.MODE_PRIVATE);
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
