package com.actiknow.clearsale.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.fragment.SignInFragment;
import com.actiknow.clearsale.fragment.SignUpFragment;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.BuyerDetailsPref;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    BuyerDetailsPref buyerDetailsPref;
    CallbackManager callbackManager;
    TextView tv1;
    RelativeLayout rlFbButton;
    RelativeLayout rlLinkedInButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> fbPermissions;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        initView ();
        initData ();
        initListener ();
        displayFirebaseRegId ();
        initFb ();
    }
    
    private void initView () {
        tabLayout = (TabLayout) findViewById (R.id.tabs);
        viewPager = (ViewPager) findViewById (R.id.viewpager);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tv1 = (TextView) findViewById (R.id.tv1);
        rlFbButton = (RelativeLayout) findViewById (R.id.rlFbButton);
        rlLinkedInButton = (RelativeLayout) findViewById (R.id.rlLinkedInButton);
    }
    
    private void initData () {
        FacebookSdk.sdkInitialize (this.getApplicationContext ());
        fbPermissions = Arrays.asList ("public_profile", "email");
        tabLayout.setupWithViewPager (viewPager);
        setupViewPager (viewPager);
        callbackManager = CallbackManager.Factory.create ();
        buyerDetailsPref = BuyerDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (LoginActivity.this);
        Utils.setTypefaceToAllViews (this, clMain);
    }
    
    private void displayFirebaseRegId () {
        Utils.showLog (Log.ERROR, "Firebase Reg ID:", buyerDetailsPref.getStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_FIREBASE_ID), true);
    }
    
    private void setupViewPager (ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter (getSupportFragmentManager ());
        adapter.addFragment (new SignInFragment (), "SIGN IN");
        adapter.addFragment (new SignUpFragment (), "SIGN UP");
        viewPager.setAdapter (adapter);
        
        viewPager.setOnPageChangeListener (new ViewPager.OnPageChangeListener () {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
            }
            
            @Override
            public void onPageSelected (int position) {
                switch (position) {
                    case 0:
                        tv1.setText ("SIGN IN");
                        break;
                    case 1:
                        tv1.setText ("SIGN UP");
                        break;
                }
            }
            
            @Override
            public void onPageScrollStateChanged (int state) {
            }
        });
    }
    
    private void initFb () {
        callbackManager = CallbackManager.Factory.create ();
        LoginManager.getInstance ().registerCallback (callbackManager,
                new FacebookCallback<LoginResult> () {
                    @Override
                    public void onSuccess (LoginResult loginResult) {
                        Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
                        GraphRequest request = GraphRequest.newMeRequest (
                                loginResult.getAccessToken (),
                                new GraphRequest.GraphJSONObjectCallback () {
                                    @Override
                                    public void onCompleted (JSONObject object, GraphResponse response) {
                                        Utils.showLog (Log.ERROR, "Facebook Response 1", response.toString (), true);
                                        Utils.showLog (Log.ERROR, "Facebook Response 2", object.toString (), true);
                                        try {
                                            buyerDetailsPref.putStringPref (getApplicationContext (), buyerDetailsPref.BUYER_IMAGE, object.getJSONObject ("picture").getJSONObject ("data").getString ("url"));
                                            buyerDetailsPref.putStringPref (getApplicationContext (), buyerDetailsPref.BUYER_FACEBOOK_ID, object.getString ("id"));
                                            try {
                                                sendFacebookDetailsToServer (object.getString ("name"), object.getString ("email"), progressDialog);
                                            } catch (Exception je) {
                                                sendFacebookDetailsToServer (object.getString ("name"), "", progressDialog);
                                                je.printStackTrace ();
                                            }
                                            
                                        } catch (JSONException e) {
                                            e.printStackTrace ();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle ();
                        parameters.putString ("fields", "id,name,email,picture.type(large)");
                        request.setParameters (parameters);
                        request.executeAsync ();
                    }
                    
                    @Override
                    public void onCancel () {
                        Utils.showSnackBar (LoginActivity.this, clMain, "Login Canceled", Snackbar.LENGTH_LONG, null, null);
                    }
                    
                    @Override
                    public void onError (FacebookException exception) {
                        Utils.showSnackBar (LoginActivity.this, clMain, "Error : " + exception.getMessage (), Snackbar.LENGTH_LONG, null, null);
                    }
                });
    }
    
    public void initLinkedIn () {
        Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
        LISessionManager.getInstance (getApplicationContext ())
                .init (this, Scope.build (Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS), new AuthListener () {
                    @Override
                    public void onAuthSuccess () {
                        try {
                            Utils.showLog (Log.ERROR, "LinkedIn Response 1", LISessionManager.getInstance (getApplicationContext ()).getSession ().getAccessToken ().toString (), true);
                            buyerDetailsPref.putStringPref (getApplicationContext (), buyerDetailsPref.BUYER_LINKEDIN_ID, new JSONObject (LISessionManager.getInstance (getApplicationContext ()).getSession ().getAccessToken ().toString ()).getString ("accessTokenValue"));
//                            Utils.showLog (Log.ERROR, "karman", "Auth token : " + new JSONObject (LISessionManager.getInstance (getApplicationContext ()).getSession ().getAccessToken ().toString ()).getString ("accessTokenValue"), true);
                            APIHelper apiHelper = APIHelper.getInstance (getApplicationContext ());
                            apiHelper.getRequest (LoginActivity.this, AppConfigURL.URL_LINKEDIN_AUTH, new ApiListener () {
                                @Override
                                public void onApiSuccess (ApiResponse result) {
                                    try {
                                        JSONObject jsonObject = new JSONObject (result.getResponseDataAsJson ().toString ());
                                        Utils.showLog (Log.ERROR, "LinkedIn Response 2", jsonObject.toString (), true);
                                        buyerDetailsPref.putStringPref (getApplicationContext (), buyerDetailsPref.BUYER_IMAGE, jsonObject.getJSONObject ("pictureUrls").getJSONArray ("values").get (0).toString ());
//                                        Utils.showLog (Log.ERROR, "karman", "name : " + jsonObject.getString ("formattedName"), true);
//                                        Utils.showLog (Log.ERROR, "karman", "email : " + jsonObject.getString ("emailAddress"), true);
                                        sendLinkedInDetailsToServer (jsonObject.getString ("formattedName"), jsonObject.getString ("emailAddress"), progressDialog);
                                    } catch (JSONException e) {
                                        e.printStackTrace ();
                                    } catch (Exception e) {
                                        e.printStackTrace ();
                                    }
                                }
                                
                                @Override
                                public void onApiError (LIApiError error) {
                                    Utils.showLog (Log.ERROR, "LinkedIn Error", error.toString (), true);
                                    Utils.showSnackBar (LoginActivity.this, clMain, "Login Failed", Snackbar.LENGTH_LONG, null, null);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }
                    }
                    
                    @Override
                    public void onAuthError (LIAuthError error) {
                        Utils.showSnackBar (LoginActivity.this, clMain, "Login Failed : " + error.toString (), Snackbar.LENGTH_LONG, null, null);
                    }
                }, true);
    }
    
    private void initListener () {
        rlFbButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (NetworkConnection.isNetworkAvailable (LoginActivity.this)) {
                    LoginManager.getInstance ().logInWithReadPermissions (LoginActivity.this, fbPermissions);
                } else {
                }
            }
        });
        
        rlLinkedInButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (NetworkConnection.isNetworkAvailable (LoginActivity.this)) {
                    initLinkedIn ();
                } else {
                }
                
            }
        });
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        callbackManager.onActivityResult (requestCode, resultCode, data);
        LISessionManager.getInstance (getApplicationContext ()).onActivityResult (this, requestCode, resultCode, data);
    }
    
    private void sendFacebookDetailsToServer (final String name, final String email, final ProgressDialog progressDialog) {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_FACEBOOK_SIGN_IN, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_FACEBOOK_SIGN_IN,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        buyerDetailsPref.putIntPref (LoginActivity.this, BuyerDetailsPref.BUYER_ID, jsonObj.getInt (AppConfigTags.BUYER_ID));
                                        buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_NAME, jsonObj.getString (AppConfigTags.BUYER_NAME));
                                        buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_EMAIL, jsonObj.getString (AppConfigTags.BUYER_EMAIL));
                                        buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_MOBILE, jsonObj.getString (AppConfigTags.BUYER_MOBILE));
                                        buyerDetailsPref.putIntPref (LoginActivity.this, BuyerDetailsPref.PROFILE_STATUS, jsonObj.getInt (AppConfigTags.PROFILE_STATUS));
        
                                        switch (jsonObj.getInt (AppConfigTags.PROFILE_STATUS)) {
                                            case 0:
                                                break;
                                            case 1:
                                                buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.PROFILE_STATE, jsonObj.getString (AppConfigTags.PROFILE_STATE));
                                                buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.PROFILE_PRICE_RANGE, jsonObj.getString (AppConfigTags.PROFILE_PRICE_RANGE));
                                                buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE, jsonObj.getString (AppConfigTags.PROFILE_HOME_TYPE));
                                                break;
                                        }
        
                                        Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity (intent);
                                        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar (LoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.BUYER_NAME, name);
                    params.put (AppConfigTags.BUYER_EMAIL, email);
                    params.put (AppConfigTags.BUYER_FACEBOOK_ID, buyerDetailsPref.getStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_FACEBOOK_ID));
                    params.put (AppConfigTags.BUYER_FIREBASE_ID, buyerDetailsPref.getStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_FIREBASE_ID));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    private void sendLinkedInDetailsToServer (final String name, final String email, final ProgressDialog progressDialog) {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_LINKEDIN_SIGN_IN, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_LINKEDIN_SIGN_IN,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        buyerDetailsPref.putIntPref (LoginActivity.this, BuyerDetailsPref.BUYER_ID, jsonObj.getInt (AppConfigTags.BUYER_ID));
                                        buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_NAME, jsonObj.getString (AppConfigTags.BUYER_NAME));
                                        buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_EMAIL, jsonObj.getString (AppConfigTags.BUYER_EMAIL));
                                        buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_MOBILE, jsonObj.getString (AppConfigTags.BUYER_MOBILE));
                                        buyerDetailsPref.putIntPref (LoginActivity.this, BuyerDetailsPref.PROFILE_STATUS, jsonObj.getInt (AppConfigTags.PROFILE_STATUS));
                                        
                                        switch (jsonObj.getInt (AppConfigTags.PROFILE_STATUS)) {
                                            case 0:
                                                break;
                                            case 1:
                                                buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.PROFILE_STATE, jsonObj.getString (AppConfigTags.PROFILE_STATE));
                                                buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.PROFILE_PRICE_RANGE, jsonObj.getString (AppConfigTags.PROFILE_PRICE_RANGE));
                                                buyerDetailsPref.putStringPref (LoginActivity.this, BuyerDetailsPref.PROFILE_HOME_TYPE, jsonObj.getString (AppConfigTags.PROFILE_HOME_TYPE));
                                                break;
                                        }
                                        
                                        Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity (intent);
                                        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar (LoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.BUYER_NAME, name);
                    params.put (AppConfigTags.BUYER_EMAIL, email);
                    params.put (AppConfigTags.BUYER_LINKEDIN_ID, buyerDetailsPref.getStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_LINKEDIN_ID));
                    params.put (AppConfigTags.BUYER_FIREBASE_ID, buyerDetailsPref.getStringPref (LoginActivity.this, BuyerDetailsPref.BUYER_FIREBASE_ID));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<> ();
        private final List<String> mFragmentTitleList = new ArrayList<> ();
    
        public ViewPagerAdapter (FragmentManager manager) {
            super (manager);
        }
    
        @Override
        public Fragment getItem (int position) {
            return mFragmentList.get (position);
        }
    
        @Override
        public int getCount () {
            return mFragmentList.size ();
        }
    
        public void addFragment (Fragment fragment, String title) {
            mFragmentList.add (fragment);
            mFragmentTitleList.add (title);
        }
    
        @Override
        public CharSequence getPageTitle (int position) {
            return mFragmentTitleList.get (position);
        }
    }
}