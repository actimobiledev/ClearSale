package com.actiknow.clearsale.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static boolean isNetworkAvailable (Context context) {
        boolean connFlag = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo ();
        if (null != activeNetwork) {
            if (activeNetwork.getType () == ConnectivityManager.TYPE_WIFI)

                connFlag = true;
            if (activeNetwork.getType () == ConnectivityManager.TYPE_MOBILE)
                connFlag = true;
        }
        if (! connFlag) {
            //         Toast.makeText(context, "Please Check Network Connection", Toast.LENGTH_LONG).show();
        }
        return connFlag;
    }

    public static int getConnStatus (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo ();
        if (null != activeNetwork) {
            if (activeNetwork.getType () == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType () == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnStatusString (Context context) {
        int conn = getConnStatus (context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
}