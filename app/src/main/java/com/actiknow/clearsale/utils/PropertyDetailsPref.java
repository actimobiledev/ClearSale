package com.actiknow.clearsale.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PropertyDetailsPref {
    
    public static String PROPERTY_ID = "property_id";
    public static String PROPERTY_STATE = "property_state";
    public static String PROPERTY_ADDRESS1 = "property_address1";
    public static String PROPERTY_ADDRESS2 = "property_address2";
    public static String PROPERTY_LATITUDE = "property_latitude";
    public static String PROPERTY_LONGITUDE = "property_longitude";
    public static String PROPERTY_PRICE = "property_price";
    public static String PROPERTY_YEAR_BUILD = "property_year_build";
    public static String PROPERTY_BEDROOM = "property_bedroom";
    public static String PROPERTY_BATHROOM = "property_bathroom";
    public static String PROPERTY_AREA = "property_area";
    public static String PROPERTY_OVERVIEW = "property_overview";
    public static String PROPERTY_COMPS = "property_comps";
    public static String PROPERTY_ACCESS = "property_access";
    public static String PROPERTY_REALTOR = "property_realtor";
    public static String PROPERTY_OFFER = "property_offer";
    public static String PROPERTY_IMAGES = "property_image";
    public static String PROPERTY_AUCTION_ID = "auction_id";
    public static String PROPERTY_AUCTION_STATUS = "auction_status";
    
    private static PropertyDetailsPref propertyDetailsPref;
    private String PROPERTY_DETAILS = "PROPERTY_DETAILS";
    
    public static PropertyDetailsPref getInstance () {
        if (propertyDetailsPref == null)
            propertyDetailsPref = new PropertyDetailsPref ();
        return propertyDetailsPref;
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
