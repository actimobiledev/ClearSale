package com.actiknow.clearsale.service;

import android.util.Log;

import com.actiknow.clearsale.utils.BuyerDetailsPref;
import com.actiknow.clearsale.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName ();
    @Override
    public void onTokenRefresh () {
        super.onTokenRefresh ();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Utils.showLog (Log.DEBUG, TAG, "Refreshed Token: " + refreshedToken, true);
    
        BuyerDetailsPref buyerDetailsPref = BuyerDetailsPref.getInstance ();
        buyerDetailsPref.putStringPref (getApplicationContext (), BuyerDetailsPref.BUYER_FIREBASE_ID, refreshedToken);
    }
}

