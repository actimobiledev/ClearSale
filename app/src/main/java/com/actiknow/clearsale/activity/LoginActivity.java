package com.actiknow.clearsale.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.clearsale.R;
import com.actiknow.clearsale.fragment.SignInFragment;
import com.actiknow.clearsale.fragment.SignUpFragment;
import com.actiknow.clearsale.utils.AppConfigTags;
import com.actiknow.clearsale.utils.AppConfigURL;
import com.actiknow.clearsale.utils.Constants;
import com.actiknow.clearsale.utils.NetworkConnection;
import com.actiknow.clearsale.utils.PrefUtil;
import com.actiknow.clearsale.utils.UserDetailsPref;
import com.actiknow.clearsale.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "facebook_login";
    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host + "/v1/people/~:" + "(email-address,formatted-name,phone-numbers,picture-urls::(original))";
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    Button linked_in_login_button;
    UserDetailsPref userDetailsPref;
    PackageInfo info;
    PrefUtil prefUtil;
    String accessToken;
    CallbackManager callbackManager;
    String id, name, email, gender, birthday;
    RelativeLayout rlFbButton;
    RelativeLayout rlLinkedInButton;
    TextView tv1;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_login);
        initView();
        initData();
        initListener();
        displayFirebaseRegId();


    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        loginButton = (LoginButton) findViewById (R.id.login_button);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile", "AccessToken");
    
        tv1 = (TextView) findViewById (R.id.tv1);
        rlFbButton = (RelativeLayout) findViewById (R.id.rlFbButton);
        rlLinkedInButton = (RelativeLayout) findViewById (R.id.rlLinkedInButton);
    }

    private void initData() {
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(LoginActivity.this);
        Utils.setTypefaceToAllViews (this, clMain);
    }

    private void displayFirebaseRegId() {
        Utils.showLog(Log.ERROR, "Firebase Reg ID:", userDetailsPref.getStringPref(LoginActivity.this, UserDetailsPref.USER_FIREBASE_ID), true);
    }
    
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment (new SignInFragment (), "SIGN IN");
        adapter.addFragment (new SignUpFragment (), "SIGN UP");
        viewPager.setAdapter(adapter);
        
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
    private void initListener() {
        loginButton.registerCallback (callbackManager, new FacebookCallback<LoginResult> () {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Toast.makeText(SignInFragment.this, "User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                accessToken = loginResult.getAccessToken().getToken();
                String userId = loginResult.getAccessToken().getUserId();
                System.out.println("onSuccess");

                // accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("SignInFragment", response.toString());
                                try {
                                    id = object.getString("id");
                                    try {
                                        URL profile_pic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                                        Log.i("profile_pic", profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    name = object.getString("name");
                                    email = object.getString("email");
                                    gender = object.getString("gender");
                                    birthday = object.getString("birthday");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                loginWithFacebookDetailSendToServer(accessToken, name, email);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                Log.d(TAG, "Login attempt failed.");
            }
        });
    }

    private void loginWithFacebookDetailSendToServer(final String accessToken, final String name, final String email) {

        if (NetworkConnection.isNetworkAvailable(LoginActivity.this)) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_FACEBOOK_SIGN_IN, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_FACEBOOK_SIGN_IN,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, "rahul" + AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        userDetailsPref.putIntPref(LoginActivity.this, UserDetailsPref.USER_ID, jsonObj.getInt(AppConfigTags.SIGN_IN_USER_ID));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_NAME, jsonObj.getString(AppConfigTags.SIGN_IN_USER_NAME));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_EMAIL, jsonObj.getString(AppConfigTags.SIGN_IN_USER_EMAIL));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_ACCESS_TOKEN, jsonObj.getString(AppConfigTags.SIGN_IN_ACCESS_TOKEN));
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        LoginActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    } else {
                                        Utils.showSnackBar(LoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put(AppConfigTags.SIGN_IN_USER_EMAIL, email);
                    params.put(AppConfigTags.SIGN_IN_NAME, name);
                    params.put(AppConfigTags.SIGN_IN_ACCESS_TOKEN, accessToken);
                    params.put(AppConfigTags.USER_FIREBASE_ID, userDetailsPref.getStringPref(LoginActivity.this, UserDetailsPref.USER_FIREBASE_ID));
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }


    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
//        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    //User logged out
                    prefUtil.clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<> ();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}







